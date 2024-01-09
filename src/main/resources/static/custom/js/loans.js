
    var issuedBooks = [];
    var selectedIssueId = undefined;
    var booksToLoan = [];

    // Loan List functions
    function returnBook(issueId) {
        selectedIssueId = issueId;
        issuedBooks = [];
        jQuery('#issue_'+issueId).find('li').each(function (i){
            var book = {id: $(this).attr('data-id'), title: $(this).text()}
            issuedBooks.push( book );
        });
        populateReturnBookTable();
        jQuery('#select-all').prop('checked', false);
        jQuery('.returnBookChk').prop('checked', false);
        $('.return-book-modal').modal('show');
    }

    function populateReturnBookTable() {
        var trs = '';
        for(var k=0 ; k<issuedBooks.length ; k++) {
            trs += '<tr>';
            trs += '<td><input type="checkbox" value="'+issuedBooks[k].id+'" class="returnBookChk" onclick="returnBookChkClicked()" /></td>';
            trs += '<td>'+issuedBooks[k].title+'</td>';
            trs += '</tr>';
        }
        jQuery('#returnBookTable tr:gt(0)').remove();
        jQuery('#returnBookTable').append(trs);
    }

    function returnBookChkClicked() {
        var total = jQuery('.returnBookChk').length;
        var checked = jQuery('.returnBookChk:checkbox:checked').length;
        if( total == checked ) {
            jQuery('#select-all').prop('checked', true);
        } else {
            jQuery('#select-all').prop('checked', false);
        }
    }

    function sellectAll() {
        if( jQuery('#select-all').prop('checked') ) {
            jQuery('.returnBookChk').prop('checked', true);
        } else {
            jQuery('.returnBookChk').prop('checked', false);
        }
    }

    function returnBookConfirm() {
        var checked = jQuery('.returnBookChk:checkbox:checked').length;
        if( checked > 0 ) {
            var total = jQuery('.returnBookChk').length;
            if( total == checked ) {
                $.get('/rest/loan/'+ selectedIssueId + '/return/all', function(msg) {
                    if( msg == 'successful' ) {
                        window.location = '/loan/list';
                    }
                });
            } else {
                var ids = [];
                jQuery('.returnBookChk:checkbox:checked').each(function (i){
                    ids.push( $(this).val() );
                });
                $.post( '/rest/loan/'+selectedIssueId+'/return' , {ids: ids.join(',')} ).done(function (msg){
                    if( msg=='successful' ) {
                        window.location = '/loan/list';
                    }
                });
            }
        }
    }

    // Loan Form functions
    var members = [];

    function initMembers() {
        $.get('/rest/member/list', function(data) {
            if (data) {
                members = data;
                populateMembersList(members); // Populate the members list
            }
        });
    }
    initMembers();

    function populateMembersList(membersList) {
        $('#memberSel').empty().append('<option value="">-- Select Member --</option>');
        $.each(membersList, function(k, v) {
            $('#memberSel').append($("<option></option>")
                .attr("value", v.id).text(v.firstName + ' ' + v.middleName + (v.lastName ? ' ' + v.lastName : '')));
        });
    }

    // On member dropdown change, you can perform the desired action
    $('#memberSel').on('change', function() {
        var selectedMemberId = $(this).val();
        console.log('Selected Member ID:', selectedMemberId);
    });

    function getBooksByCategory(value) {
        $.get('/rest/book/' + value + '/available', function(data) {
            if( data ) {
                populateBooksList( data );
            }
        });
    }

    function populateBooksList( booksList ) {
        $('#booksSel').empty().append('<option selected="selected" value="">-- Select Book --</option>');
        $.each(booksList, function(k, v) {   
            $('#booksSel').append($("<option></option>")
                .attr("value",v.id).text(v.title)
                .attr("data-authors", v.authors)
                .attr("data-tag", v.tag)
                .attr("data-publisher", v.publisher));
        });
    }

    $('#categorySel').on('change', function(){
        var value = $(this).val();
        if( value ) {
            var books = getBooksByCategory( value );
        } else {
            populateBooksList( [] );
        }
    });

    $('#addBookBtn').on('click', function() {
        var id = $('#booksSel').val();
        var title = $("#booksSel option:selected").text();
        var tag = $("#booksSel option:selected").attr("data-tag");
        var authors = $("#booksSel option:selected").attr("data-authors");
        
        if( id && !bookAlreadyExist(id) && title && tag && authors ) {
            var book = { id: id, title: title, tag: tag, authors: authors };
            booksToLoan.push(book);
            $('#booksSel').val('');
            initBooksInTable();
        }
    });

    function bookAlreadyExist(id) {
        for(var k=0 ; k<booksToLoan.length ; k++) {
            if( booksToLoan[k].id == id ) {
                return true;
            }
        }
        return false;
    }

    $('#saveBtn').on('click', function(){
        var errors = validate();
        if( errors.length > 0 ) {
            $('.errors-modal').find('.modal-body').html( errors.join('<br />') );
            $('.errors-modal').modal('show');
        } else {
            var loan = { 
                member: $('#memberSel').val(),
                books: getLoandBookIds().join()
            }
            $.post( "/rest/loan/save", loan).done(function (data){
                if( data=='success' ) {
                    window.location = '/loan/new';
                }
            });
        }
    });

    function getLoandBookIds() {
        var ids = [];
        for(var k=0 ; k<booksToLoan.length ; k++) {
            ids.push( booksToLoan[k].id );
        }
        return ids;
    }

    function validate() {
        var errors = []
        var member = $('#memberSel').val();
        if( !member ) {
            errors.push('- Select Member');
        }
        if( booksToLoan.length == 0 ) {
            errors.push('- Add Books to loan');
        }
        return errors;
    }

    function initBooksInTable() {
        var trs = '';
        for( var k=0 ; k<booksToLoan.length ; k++ ) {
            var rowNum = k+1;
            trs += '<tr>';
            trs += '<td>'+rowNum+'</td>';
            trs += '<td>'+booksToLoan[k].tag+'</td>';
            trs += '<td>'+booksToLoan[k].title+'</td>';
            trs += '<td>'+booksToLoan[k].authors+'</td>';
            trs += '<td><a href="javascript:void(0)"  onclick="removeFromTable('+rowNum+', '+booksToLoan[k].id+')"><i class="fa fa-remove"></i></a></td>';
            trs += '</tr>';
        }
        $("#loanBooksTable").find("tr:gt(0)").remove();
        $('#loanBooksTable').append( trs );
    }

    function removeFromTable(rowNum, id) {
        $('#loanBooksTable tr:eq('+(rowNum)+')').remove();
        removeFromBooksLoandList(id);
        initBooksInTable();
    }

    function removeFromBooksLoandList(id) {
        for( var k=0 ; k<booksToLoan.length ; k++ ) {
            if( booksToLoan[k].id == id ) {
                booksToLoan.splice(k, 1);
                break;
            }
        }
    }
