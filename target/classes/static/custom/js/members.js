$(document).ready(function() {
    // Save button click event for member form
    $('#saveBtn').on('click', function() {
        if (isValidateBirthDate()) {
            $('#member-form').submit();
        } else {
            $('#dobErr').text('Invalid Date Format');
        }
    });

    // Validate birth date function
    function isValidateBirthDate() {
        var dateStr = $('#dateOfBirth').val();
        var timestamp = Date.parse(dateStr);
        return !isNaN(timestamp);
    }

    // Go to list page button click event
    $('#gotoListBtn').on('click', function() {
        window.location = "/members"; // Assuming the endpoint is /members for both list and form
    });

    // Remove member dialog setup
    var memberId, memberName;

    $('.remove-member-modal').on('show.bs.modal', function(event) {
        var button = $(event.relatedTarget);
        memberId = button.data('member-id');
        memberName = button.data('member-name');
        $(this).find('#member-name').text(memberName);
    });

    // Remove member button click event
    $('#removeMemberBtn').on('click', function() {
        $('.remove-member-modal').modal('hide');
        window.location = "/members/remove/" + memberId; // Assuming the endpoint is /members/remove
    });
});
