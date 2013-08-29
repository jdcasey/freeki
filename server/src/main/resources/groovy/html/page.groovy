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
    <link rel="stylesheet" href="/static/css/pagedown.css">
  </head>
  <body>
<% def last = '/wiki/' %>
<a href="${last}">Wiki Root</a> /<% data.group.split('/').each { last = last + it + '/' %> <a href="${last}">${it}</a> /<% } %>
<div id="page-content">
</div>
<div id="buttonbar-edit-page" class="buttonbar">
  <button id="edit-page">Edit</button>
  <button id="delete-page">Delete</button>
</div>
<div id="page-edit" style="display:none">
  <div class="wmd-panel">
    <div id="wmd-button-bar"></div>
    <textarea class="wmd-input" id="wmd-input">
${data.content}
</textarea>
  </div>
  <div id="wmd-preview" class="wmd-panel wmd-preview"></div>
  <div id="buttonbar-editor-controls" class="buttonbar">
    <button id="save-edit">Save</button>
    <button id="cancel-edit">Cancel</button>
    <button id="delete-edit">Delete</button>
  </div>
</div>

  <span style="font-size: small; float: center;">
    Last updated: ${data.updated} by ${data.currentAuthor}.
  </span>
  <script type="text/javascript" src="/static/js/wikiMain.js"></script>
  <script>
    \$(document).ready(function(){
      init('/api/page/${data.id}', '/wiki/${data.group}/', '${data.group}' );
    });
  </script>
</body>
</html>