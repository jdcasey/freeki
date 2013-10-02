<% 
/*******************************************************************************
 * Copyright (C) 2013 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
def mainPages = [ 'Overview', 'Index', 'Main', 'README' ]
 
def last = '/wiki/'
def path = data.name.split('/')
def me = path.length > 0 ? path[path.length-1] : 'Wiki Root'
if ( path.length > 0 ){
  def newpath = new String[path.length-1]
  System.arraycopy(path,0,newpath,0,path.length-1)
  path = newpath
}

def mainPg;
if ( data.children ){
  data.children.find {
    if (it.type.name().equals("PAGE") && mainPages.contains(it.id) ){
      System.out.println( "Found main page: ${it.id}" )
      mainPg = it.id
      return true
    }
    
    System.out.println("NOT main: ${it.id}")
    return false
  }
}
%>
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
    <link rel="stylesheet" href="/static/css/wikiMain.css"/>
    <link rel="stylesheet" href="/static/css/branding.css"/>
  </head>
<body>
<div id="group-branding-header" class="branding-header">
  <span class="freeki-plug">Look, another <a target="_new" href="https://github.com/jdcasey/freeki">Freeki</a> portable wiki!</span>
</div>

<% if(!data.name.equals("/")){ %>
<div id="group-breadcrumbs" class="breadcrumbs">
<a class="breadcrumb-root breadcrumb" href="${last}">(Root)</a><span class="breadcrumb-sep">/</span><% path.each { last = last + it + '/' %> <a class="breadcrumb" href="${last}">${it}</a><span class="breadcrumb-sep">/</span><% } %><span class="breadcrumb">${me}</span><span class="breadcrumb-sep">/</span>
</div>
<% } %>

<div id="group-main">
<% if ( data.children ){ %>
<div id="group-listing">
  <div id="group-listing-title">Available Pages:</div>
  <ul>
  <% data.children.each { %>
    <li><a href="${it.id}<% if (it.type.name().equals("GROUP")) { %>/<% } else { %>#<% } %>">${it.label}<% if (it.type.name().equals("GROUP")) { %>/<% } %></a><% } %></li>
  </ul>
</div>
<div id="group-main-content">
<% if(mainPg){ %>
  <div class="embedded-content" page="${mainPg}" class="main-content"><span style="color: red;">Loading ${mainPg}. Please wait...</span></div>
<% } else { %>
  Select a page to view.
<% } %>
</div>
<% } %>
<% if( !readOnly ){ %>
</div>
<div id="buttonbar-group-view" class="buttonbar">
  <button id="group-new-form-trigger">New...</button>
  <button id="delete-group">Delete</button>
  <button id="push-updates">Push Updates</button>
</div>
<div id="group-new-panel" style="display:none">
  <form id="group-new-form" class="microform">
    <div class="form-line" id="new-title"><label>Title</label><input type="text" cols="40" id="group-new-title"/></div>
    <div class="form-line">
      <input type="radio" name="newtype" id="group-new-group" value="group" checked="true">Group</input>
      <input type="radio" name="newtype" id="group-new-page" value="page">Page</input>
      <input type="radio" name="newtype" id="group-new-template" value="template">From Template...</input>
    </div>
    <div class="form-line" id="template-selector-field" style="display:none">
      <label>Choose a Template:</label><select id="templates-list"><option>Select a template</option></select>
    </div>
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
  <div id="user-pass-panel" style="display:none" class="template">
    <form id="user-pass-form">
      <div class="form-line"><span id="user-pass-instructions" class="instructions"></span></div>
      <div class="form-line"><label>Username:</label><input id="user" name="user" type="text" cols="20"></input></div>
      <div class="form-line"><label>Password:</label><input id="password" name="password" type="password" cols="30"></input></div>
      <div class="buttonbar microform-buttons">
        <button id="user-pass-submit">Ok</button>
        <button id="user-pass-cancel">Cancel</button>
      </div>
    </form>
  </div>
<% } %>
    <script type="text/javascript" src="/static/js/wikiMain.js"></script>
    <script>
        \$(document).ready(function(){
          init('/api/group/${data.name}', '/wiki/${data.parent}/', '${data.name}', ${readOnly} );
        });
    </script>
    <script type="text/javascript" src="/static/js/branding.js"></script>
    <script type="text/javascript" src="/static/js/group-extras.js"></script>
</body>
</html>