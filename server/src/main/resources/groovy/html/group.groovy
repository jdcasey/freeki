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
    <script type="text/javascript" src="/static/js/jquery.js"></script>
    <script type="text/javascript" src="/static/js/jquery-ui.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Converter.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Sanitizer.js"></script>
    <script type="text/javascript" src="/static/js/Markdown.Editor.js"></script>
    
    <link rel="stylesheet" href="/static/css/jquery-ui.css"/>
    <link rel="stylesheet" href="/static/css/pagedown.css"/>
    <link rel="stylesheet" href="/static/css/branding.css"/>
  </head>
<body>
<div id="group-branding-header" class="branding-header">
  <span class="freeki-plug">Look, another <a target="_new" href="https://github.com/jdcasey/freeki">Freeki</a> portable wiki!</span>
</div>
<div id="group-header-panel">
  <span id="group-name"><% if (me == 'Wiki Root') { %>Wiki Root<% }else{ %>${data.name}<% } %></span>
</div>

<div id="group-breadcrumbs" class="breadcrumbs">
<% if(!data.name.equals("/")){ %><a class="breadcrumb-root breadcrumb" href="${last}">Wiki Root</a><span class="breadcrumb-sep">/</span><% path.each { last = last + it + '/' %> <a class="breadcrumb" href="${last}">${it}</a><span class="breadcrumb-sep">/</span><% }} %>
</div>

<div id="group-main" class="main-content">
<% if ( data.children ){ %>
<div id="group-listing">
  <ul>
  <% data.children.each { %>
    <li><a href="${it.id}<% if (it.type.name().equals("GROUP")) { %>/<% } %>">${it.label}<% if (it.type.name().equals("GROUP")) { %>/<% } %></a><% } %></li>
  </ul>
</div>
<% } %>
<% if( !readOnly ){ %>
<div id="buttonbar-group-view" class="buttonbar">
  <button id="group-new-form-trigger">New...</button>
  <button id="delete-group">Delete</button>
</div>
</div>
<div id="group-new-panel" style="display:none">
  <form id="group-new-form" class="microform">
    <span class="form-line"><label>Title</label><input type="text" cols="40" id="group-new-title"/></span>
    <span class="form-line">
      <input type="radio" name="newtype" id="group-new-grouptype" value="group" checked="true">Group</input>
      <input type="radio" name="newtype" id="group-new-pagetype" value="page">Page</input>
      <input type="radio" name="newtype" id="group-new-template" value="template">From Template...</input>
    </span>
    <span class="form-line" id="template-selector-field" style="display:none">
      <label>Choose a Template:</label><select id="templates-list"><option>Select a template</option></select>
    </span>
    <div class="buttonbar microform-buttons">
      <button id="group-new-submit">Create</button>
      <button id="group-new-cancel">Cancel</button>
    </div>
  </form>
</div>
<% } %>
<footer class="group-footer">
  <div class="generated-on">
  <span style="font-size: small;">
    Generated on ${new Date()}.
  </span>
  </div>
<div class="freeki-branding-help"><span style="font-size: 8pt; float: left;"><b>NOTE:</b> You can turn these off by editing \$HOME/freeki/.branding/static/css/branding.css and adding:
<pre>
.freeki-branding-help, .freeki-plug{display:none;}
</pre>
</span>
</div>
</footer>

<% if (!readOnly){ %>
<!-- hidden panels -->
  <div id="template-panel" style="display:none">
    <form id="template-form">
    </form>
    <div class="buttonbar">
      <button id="template-form-submit">Create</button>
      <button id="template-form-cancel">Cancel</button>
    </div>
  </div>

  <script type="text/javascript" src="/static/js/wikiMain.js"></script>
  <script>
    \$(document).ready(function(){
      init('/api/group/${data.name}', '/wiki/${data.parent}/', '${data.name}' );
    });
  </script>
 <% } %>
  <script type="text/javascript" src="/static/js/branding.js"></script>
  <script type="text/javascript" src="/static/js/group-extras.js"></script>
</body>
</html>