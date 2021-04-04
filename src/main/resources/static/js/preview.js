$(function() {


  // アップロードするファイルを選択
  $('input[type=file]').change(function() {
    var file = $(this).prop('files')[0];

    // 画像以外は処理を停止
    if (! file.type.match('image.*')) {
      return;
    }

    // 画像表示
    var reader = new FileReader();
    reader.onload = function() {
      $('#img-preview').attr('src', reader.result).attr('class', 'card-img-top');
    }
    reader.readAsDataURL(file);
  });

	$(document).ready(function() {
        displayPreview();
	});
    $(document).on('input', '#menu', function(e) {
        displayPreview();
    });
    $(document).on('input', '#shopName', function(e) {
		displayPreview();
    });
    $(document).on('input', '#comment', function(e) {
		displayPreview();
    });
	$('[name="literatureDisplayFlag"]').change(function(){
        displayPreview();
	});

    function displayPreview() {
    	var tweetText = generateTweetMsg();
		$('#wordCount').text(tweetText.length);
		$('#progress-tweet').text(tweetText.length + 'chars');
		$('#progress-tweet').attr('aria-valuenow', ""+tweetText.length);
		$('#progress-tweet').width(((tweetText.length * 100) / 150) + '%');
		if (tweetText.length > 150) {
			$('#progress-tweet').addClass('bg-danger');
			$('#gourmet-submit').prop("disabled", true);
		} else {
			$('#progress-tweet').removeClass('bg-danger');
			$('#gourmet-submit').prop("disabled", false);
		}
		$('#output').text(tweetText);
    }

    function generateTweetMsg() {

        var templateMsg=$('#templateMsg_'+$('#templateIndex').val()).val();
    	var menu = $('#menu').val();
    	var shopName = $('#shopName').val();
    	var comment = $('#comment').val();

    	var literature = $('#literatureDisplayFlag1').prop("checked")　? "\n※情報提供者：@" + $('#userId').val() : '';

        return templateMsg
        .replace("${shopName}", shopName)
        .replace("${menu}", menu)
        .replace("${comment}", comment)
        .replace(/\\n/g, '\n')
        + literature
        ;
    }

    $(".nav-link").click(function(){
      $(".nav-link").removeClass('active');
      $(this).addClass('active');

      $('#templateIndex').val($('.nav-link').index(this));

        displayPreview();
    });

});