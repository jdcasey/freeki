<!DOCTYPE html>
<html>
  <head>
    <title>${data.title}</title>
    <script type="text/javascript" src="/static/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/static/js/jquery-ui-1.10.3.custom.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Converter.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Sanitizer.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Editor.js"></script>
    
    <link rel="stylesheet" href="/static/css/jquery-ui-1.10.3.custom.css">
  </head>
  <body>
<% def last = '/wiki/' %>
<a href="${last}">Wiki Root</a> /<% data.group.split('/').each { last = last + it + '/' %> <a href="${last}">${it}</a> /<% } %>
<div id="page-content">
${rendered}
</div>
<div id="buttonbar-edit-page">
<button id="edit-page">Edit</button>
</div>
<div id="page-edit" style="display:none">
  <div class="wmd-panel">
    <div id="wmd-button-bar"></div>
    <textarea class="wmd-input" id="wmd-input">
# This is a test.
</textarea>
  </div>
  <div id="wmd-preview" class="wmd-panel wmd-preview"></div>
  <div id="buttonbar-editor-controls"><button id="save-edit">Save</button><button id="cancel-edit">Cancel</button></div>
</div>

  <span style="font-size: small; float: center;">
    Created on ${data.created}. Last updated: ${data.updated} by ${data.currentAuthor}.
  </span>
  <script>
    var converter = new Markdown.Converter();
    var editor = new Markdown.Editor(converter);
    editor.run();
    
    \$('#edit-page').click(function(){
      \$('#page-content').hide();
      \$('#buttonbar-edit-page').hide();
      \$('#page-edit').show();
    });
    
    \$('#cancel-edit').click(function(){
      \$('#page-edit').hide();
      \$('#page-content').show();
      \$('#buttonbar-edit-page').show();
    });
  </script>
</body>
</html>