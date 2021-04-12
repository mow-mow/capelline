$(function() {


	$(document).ready(function() {
        displayPreview();
	});
    $(document).on('input', '#msg_textarea', function(e) {
        displayPreview();
    });

    function displayPreview() {
    	var tweetText = generateTweetMsg(templateMsg=$('#msg_textarea').val());
	//	$('#wordCount').text(tweetText.length);

		$('#progress-tweet').text(tweetText);
		$('#progress-tweet').text(tweetText.length + 'chars');
		$('#progress-tweet').attr('aria-valuenow', ""+tweetText.length);
		$('#progress-tweet').width(((tweetText.length * 100) / 128) + '%');
		if (tweetText.length > 128) {
			$('#progress-tweet').addClass('bg-danger');
			$('#gourmet-submit').prop("disabled", true);
		} else {
			$('#progress-tweet').removeClass('bg-danger');
			$('#gourmet-submit').prop("disabled", false);
		}
		$('#preview_msg').text(tweetText);
    }

    function generateTweetMsg(templateMsg) {
        var orig = "🍝";
        var startChar = templateMsg.indexOf('[', 0);
        var endChar = templateMsg.indexOf(']', 0);

        if (startChar==-1 || endChar==-1) {
            return templateMsg;
        }
        templateMsg = templateMsg.substr(0, startChar) + orig.repeat(endChar - (startChar + 2-1)) + templateMsg.substr(endChar+1, templateMsg.length-1);
        return generateTweetMsg(templateMsg);
    }
});