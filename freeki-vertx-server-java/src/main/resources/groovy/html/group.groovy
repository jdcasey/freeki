<% 
def last = '/wiki/'
def path = data.name.split('/')
def me = path.length > 0 ? path[path.length-1] : 'Wiki Root'
if ( path.length > 0 ){
  def newpath = new String[path.length-1]
  System.arraycopy(path,0,newpath,0,path.length-1)
  path = newpath
} %>
<html>
  <head>
  <title>${me}</title>
    <script type="text/javascript" src="/static/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="/static/js/jquery-ui-1.10.3.custom.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Converter.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Sanitizer.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Editor.js"></script>
    
    <link rel="stylesheet" href="/static/css/jquery-ui-1.10.3.custom.css">
    <link rel="stylesheet" href="/static/css/pagedown.css">
  </head>
<body>
<header id="header"></header>
<div id="breadcrumbs">
<% if(!data.name.equals("/")){ %><a href="${last}">Wiki Root</a> /<% path.each { last = last + it + '/' %> <a href="${last}">${it}</a> /<% }} %>
</div>

<h1><% if (me == 'Wiki Root') { %>Wiki Root<% }else{ %>${data.name}<% } %></h1>

<% if ( data.children ){ %>
<ul>
<% data.children.each { %>
  <li><a href="${it.id}<% if (it.type.name().equals("GROUP")) { %>/<% } %>">${it.label}<% if (it.type.name().equals("GROUP")) { %>/<% } %></a><% } %></li>
</ul>
<% } %>

<div id="buttonbar-group-view" class="buttonbar">
  <button id="group-new-form-trigger">New...</button>
  <button id="delete-group">Delete</button>
</div>
<div id="group-new-panel" style="display:none">
  <form id="group-new-form" class="microform">
    <span class="form-line"><label>Title</label><input type="text" cols="40" id="group-new-title"/></span>
    <span class="form-line">
      <input type="radio" name="newtype" id="group-new-grouptype" value="group" checked="true">Group</input>
      <input type="radio" name="newtype" id="group-new-pagetype" value="page">Page</input>
    </span>
    <div class="buttonbar microform-buttons">
      <button id="group-new-submit">Create</button>
      <button id="group-new-cancel">Cancel</button>
    </div>
  </form>
</div>

<footer><span style="font-size: small; float: center;">
Generated on ${new Date()}.
</span></footer>

  <script type="text/javascript" src="/static/js/wikiMain.js"></script>
  <script>
    \$(document).ready(function(){
      init('/api/group/${data.name}', '/wiki/${data.parent}/', '${data.name}' );
    });
  </script>
</body>
</html>