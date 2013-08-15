var pageContent = '# Load ERROR';
var editingPageContent;

var converter = new Markdown.Converter();
converter.hooks.chain("preConversion", function (text) {
	editingPageContent = text;
  return text;
});

var editor = new Markdown.Editor(converter);
editor.run();

var pageUrl;

function init( url ){
	pageUrl = url;
	pageContent = $('#wmd-input').text();
	
	var rendered = converter.makeHtml(pageContent);
	$('#page-content').html(rendered);
}

$('#edit-page').click(function(){
  $('#page-content').hide();
  
  $('#buttonbar-edit-page').hide();
  
  $('#wmd-input').text(pageContent);
  $('#page-edit').show();
});

$('#cancel-edit').click(function(){
  $('#page-edit').hide();
  $('#page-content').show();
  $('#buttonbar-edit-page').show();
});

$('#save-edit').click(function(){
//  pageContent = $('#wmd-input').text();
  
  $.post(pageUrl, editingPageContent, function(data, textStatus){
      pageContent = editingPageContent;
  	  $('#page-content').html(converter.makeHtml(pageContent));
  	}, 
  	'text'
  );
  
  $('#page-edit').hide();
  $('#page-content').show();
  $('#buttonbar-edit-page').show();
});