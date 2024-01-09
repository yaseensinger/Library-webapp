var bookId, bookName;

function removeBookDialog(el) {
    bookId = $(el).attr('data-book-id');
    bookName = $(el).attr('data-book-name');
    $('.remove-book-modal').find('#book-name').text(bookName);
}

function removeBook() {
    $('.remove-book-modal').modal('hide');
    window.location = "/book/remove/" + bookId;
}

$(document).ready(function () {
    $('#gotoListBtn').on('click', function () {
        window.location = "/book/list";
    });

    $('#category-selectbox').on('change', function () {
        updateTagField();
    });

    $('#resetBtn').on('click', function () {
        setTimeout(function () {
            updateTagField();
        }, 10);
    });

    function updateTagField() {
        var shortName = $("#category-selectbox option:selected").attr("short-name");
        if (shortName) {
            $('#tag').val(shortName + '-');
        }
    }

    if (!$('#id').val()) {
        updateTagField();
    }
});
