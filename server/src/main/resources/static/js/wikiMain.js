var pageContent;
var editingPageContent;

var converter;
var editor;

var myUrl;
var parentUrl;
var group;

function init( url, parent, groupName ){
	myUrl = url;
	parentUrl = parent;
	group = groupName;
	
	$('#editor-content').tabs();
	
	var mdPane = $('#wmd-input');
	if ( mdPane ){
		converter = new Markdown.Converter();
		converter.hooks.chain("preConversion", function (text) {
//			alert("Changed to: " + text );
			editingPageContent = text;
		  return text;
		});
		
		editor = new Markdown.Editor(converter, "", {
			title: "Wiki Formatting Help",
			handler: function(click){
				alert("Help clicked: " + JSON.stringify( click ) );
			}
		});
		editor.run();
		
		pageContent = $(mdPane).text();
		
		var rendered = converter.makeHtml(pageContent);
		
		$('#page-content').html(rendered);
		
		if ( window.location.hash == '#editing' ){
			$('#edit-page').click();
		}
		else
		{
			$('#cancel-edit').click();
		}
	}
}

$('.preview-button').click(function(){
	// bit non-obvious, but pagedown updates this pane in realtime, so we pull
	// the html from there to display in our own preview pane. This wmd preview
	// pane is display:none to suppress it in favor of our own.
	var content = $('#wmd-preview').html();
	var rendered = converter.makeHtml(content);
	
	$('#preview-panel').html(rendered);
});

$('#edit-page').click(function(){
  $('#page-content').hide();
  
  $('#buttonbar-edit-page').hide();
  
  window.location.hash = '#editing';
  $('#wmd-input').text(pageContent);
  $('#page-edit').show();
});

$('#cancel-edit').click(function(){
  $('#page-edit').hide();
  window.location.hash = null;
  $('#page-content').show();
  $('#buttonbar-edit-page').show();
});

$('#save-edit').click(function(){
	alert( "Updated content:\n\n" + editingPageContent );
  
  $.post(myUrl, editingPageContent, function(data, textStatus){
      pageContent = editingPageContent;
  	  $('#page-content').html(converter.makeHtml(pageContent));
  	}, 
  	'text'
  );
  
  window.location.hash = null;
  $('#page-edit').hide();
  $('#page-content').show();
  $('#buttonbar-edit-page').show();
});

$('#delete-page,#delete-edit,#delete-group').click(function(){
	if( confirm( "Really delete?" ) ){
		$.ajax({
			type: 'delete',
			url: myUrl,
		}).done(function(data,textstatus){
			window.location.replace(parentUrl);
		}).fail(function(data,textstatus,error){
			alert("Delete failed: " + textstatus);
			if ( textstatus == 'error'){
				alert(error);
			}
		});
	}
});

$('#group-new-form-trigger').click(function(){
	$('#group-new-panel').toggle();
});

$('#group-new-template').click(function(){
	$.ajax({
		type: 'get',
		url: '/templates',
	}).done(function(data,textstatus){
		var templates = JSON.parse(data)['items'];
		
		$(templates).each(function(idx,template){
			var option = $('<option></option>').attr('value', template).text(template);
			if ( idx == 0 ){
				$(option).attr('selected', true);
			}
			$('#templates-list').append(option);
		});
		
		$('#template-selector-field').show();
		
	}).fail(function(data,textstatus,error){
		alert("Template listing failed: " + textstatus);
		if ( textstatus == 'error'){
			alert(error);
		}
	});
});

$('#group-new-form').submit(function(){
	return false;
});

$('#group-new-cancel').click(function(){
	$('#group-new-panel').hide();
	$('#group-new-title').text('');
	$('#group-new-grouptype').attr('checked', 'true')
	$('#group-new-pagetype').removeAttr('checked')
});

$('#group-new-submit').click(function(){
	var title = $('#group-new-title').val();
	if ( $('#group-new-grouptype').prop('checked') ){
		createGroup(title);
	}
	else if ($('#group-new-template').prop('checked') ){
		showTemplateForm();
	}
	else{
		createPage(title);
	}
});

$('#template-form-submit').click(function(){
	$('#template-form').submit();
});

$('#template-form-cancel').click(function(){
	$('#template-panel').dialog("destroy");
});

function createGroup(title){
	$.ajax({
		type: 'POST',
		url: "/api/group/" + group + "/" + title,
		data: {}
	}).done(function(data,textstatus,jqxhr){
		var url = jqxhr.getResponseHeader('Location');
		if ( url ){
			window.location=url;
		}
	}).fail(function(data,textstatus,error){
		alert("Group creation failed: " + textstatus);
		if ( textstatus == 'error'){
			alert(error);
		}
	});
}

function createPage(title){
	$.ajax({
		type: 'POST',
		url: "/api/page/" + group + "/" + title,
		data: '#' + title + '\n\nAdd content here.',
		dataType: 'text',
	}).done(function(data,textstatus,jqxhr){
		var url = jqxhr.getResponseHeader('Location');
		if ( url ){
			window.location=url + "#editing";
		}
	}).fail(function(data,textstatus,error){
		alert("Page creation failed: " + textstatus);
		if ( textstatus == 'error'){
			alert(error);
		}
	});
}

function showTemplateForm(){
	var template = $('#templates-list').prop('value');
	var templateFormUrl = '/templates/' + template;
	
	$.ajax({
		type: 'get',
		url: templateFormUrl,
	}).done(function(data,textstatus){
		$('#template-form').attr('action', templateFormUrl).attr('method', 'POST').html(data);

		$('#template-panel').dialog({
			height: 600,
			width: 600,
			modal: true,
		});
	}).fail(function(data,textstatus,error){
		var msg = "Template form retrieval failed: " + textstatus;
		if ( textstatus == 'error'){
			alert(msg + "\n\n" + error);
		}
		else{
			alert(msg);
		}
	});
}