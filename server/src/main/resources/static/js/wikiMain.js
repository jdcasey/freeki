// 
// Copyright (C) 2013 John Casey.
// 
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
// 

var pageContent;
var editingPageContent;

var converter;
var editor;

var myUrl;
var parentUrl;
var group;

var readonly;

function init( url, parent, groupName, readOnly ){
	myUrl = url;
	readonly = readOnly;
//	alert( "myUrl is: " + myUrl );
	
	parentUrl = parent;
	group = groupName;
	
	$('div.embedded-content').each(function(){
		var pg = $(this).attr('page').replace(' ', '%20');
		if(group != '/' ){
			pg = group + '/' + pg;
		}
		
		$(this).load('/wiki/' + pg + '?format=content-only' );
	});
	
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
	if ( readonly ){return;}
	
	// bit non-obvious, but pagedown updates this pane in realtime, so we pull
	// the html from there to display in our own preview pane. This wmd preview
	// pane is display:none to suppress it in favor of our own.
	var content = $('#wmd-preview').html();
	var rendered = converter.makeHtml(content);
	
	$('#preview-panel').html(rendered);
});

$('#edit-page').click(function(){
	if ( readonly ){return;}
	
  $('#page-content').hide();
  
  $('#buttonbar-edit-page').hide();
  
  window.location.hash = '#editing';
  $('#wmd-input').text(pageContent);
  $('#page-edit').show();
});

$('#cancel-edit').click(function(){
	if ( readonly ){return;}
	
  $('#page-edit').hide();
  window.location.hash = null;
  $('#page-content').show();
  $('#buttonbar-edit-page').show();
});

$('#save-edit').click(function(){
	if ( readonly ){return;}
	
//	alert( "Updated content:\n\n" + editingPageContent );
  
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
	if ( readonly ){return;}
	
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
	if ( readonly ){return;}
	
	$('#group-new-panel').toggle();
});

$('#group-new-group,#group-new-page').click(function(){
	$('#template-selector-field').hide();
	$('#new-title').show();
});

$('#group-new-template').click(function(){
	if ( readonly ){return;}
	
	$.ajax({
		type: 'get',
		url: '/templates',
	}).done(function(data,textstatus){
		var templates = JSON.parse(data)['items'];
		
		var templateList = $('#templates-list');
		$(templateList).html('');
		
		$(templates).each(function(idx,template){
			var option = $('<option></option>').attr('value', template).text(template);
			if ( idx == 0 ){
				$(option).attr('selected', true);
			}
			
			$(templateList).append(option);
		});
		
		$('#template-selector-field').show();
		$('#new-title').hide();
		
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
	if ( readonly ){return;}
	
	$('#group-new-panel').hide();
	$('#group-new-title').text('');
	$('#group-new-group').attr('checked', 'true')
	$('#group-new-page').removeAttr('checked')
});

$('#group-new-submit').click(function(){
	if ( readonly ){return;}
	
	var title = $('#group-new-title').val();
	if ( $('#group-new-group').prop('checked') ){
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
	if ( readonly ){return;}
	
	$('#template-form').submit();
});

$('#template-form-cancel').click(function(){
	if ( readonly ){return;}
	
	$('#template-panel').dialog("destroy");
});

$("#push-updates").click(function(){
	if ( readonly ){return;}
	
	collectUserPass("Enter credentials for this Git repository's owner:", function(userpass){
		$.ajax({
			type: 'POST',
			url: "/update/push",
			headers: userpass,
			data: {}
		}).done(function(data,textstatus,jqxhr){
			var url = jqxhr.getResponseHeader('Location');
			if ( url ){
				window.location=url;
			}
		}).fail(function(data,textstatus,error){
			if ( textstatus == 'error'){
				alert("Push failed: " + textstatus + "\nError: " + error);
			}
			else{
				alert("Push failed: " + textstatus);
			}
		});
	});
});

$('#user-pass-form').submit(function(){
	return false;
});

$('#user-pass-submit').click(function(){
	$('#user-pass-panel').dialog("close");
});

$('#user-pass-cancel').click(function(){
	$('#user-pass-panel').dialog("destroy");
});

function collectUserPass( title, callable ){
	if ( readonly ){return;}
	
	$('#user-pass-instructions').text(title);
	$('#user-pass-panel').dialog({
		title: "Enter Username and Password",
		height: 'auto',
		width: 'auto',
		resize: 'auto',
		modal: true,
//		buttons: [
//		  { 
//		  	text: "Ok", 
//		  	click: function() {
//		  	  $( this ).dialog( "close" );
//		  	}
//		  },
//		  { 
//		  	text: "Cancel", 
//		  	click: function() {
//		  	  $( this ).dialog( "destroy" ); 
//		  	}
//		  },
//		],
		close: function( event, ui ){
  		var user = $('#user').val();
  		var pass = $('#password').val();
  		
  		if (user != '' && pass != '' ){
  			userpass = {
  					"user": user,
  					"password": pass,
  			};
  			
  			$('#user-pass-instructions').text('');
  			$('#user').val('');
  			$('#password').val('');
  			
  			callable(userpass);
  		}
  		
			$(this).dialog("destroy");
		},
	});
}

function createGroup(title){
	if ( readonly ){return;}
	
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
	if ( readonly ){return;}
	
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
	if ( readonly ){return;}
	
	var template = $('#templates-list').prop('value');
	var templateFormUrl = '/templates/' + template;
	
	$.ajax({
		type: 'get',
		url: templateFormUrl,
	}).done(function(data,textstatus){
		$('#template-form').attr('action', templateFormUrl).attr('method', 'POST').html(data);

		$('#template-panel').dialog({
			title: template,
			height: 'auto',
			width: 'auto',
			resize: 'auto',
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
