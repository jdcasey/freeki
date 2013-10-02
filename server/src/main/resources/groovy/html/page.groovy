<!DOCTYPE html>
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
%>
<% 
def contentOnly = 'content-only'.equals(params.get('format'))
System.out.println(params)
if (!contentOnly){
%>
<html>
  <head>
    <title>${data.title}</title>
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
 <% } %>
  <body>
<% if (!contentOnly){ %>
<div id="page-branding-header" class="branding-header">
  <span id="freeki-plug">Look, another <a target="_new" href="https://github.com/jdcasey/freeki">Freeki</a> portable wiki!</span>
</div>
<div id="page-breadcrumbs" class="breadcrumbs">
<% def last = '/wiki/' %>
<a class="breadcrumb-root breadcrumb" href="${last}">(Root)</a><span class="breadcrumb-sep">/</span><% data.group.split('/').each { last = last + it + '/' %> <a class="breadcrumb" href="${last}">${it}</a><span class="breadcrumb-sep">/</span><% } %><span class="breadcrumb">${data.localId}</span>
</div>
<% } %>
<div id="page-main" class="main-content">
<% if( contentOnly || readOnly){ %>
<div id="page-content">
  ${rendered}
</div>
<% } else { %>
<div id="page-content">
</div>
<div id="buttonbar-edit-page" class="buttonbar">
  <button id="edit-page">Edit</button>
  <button id="delete-page">Delete</button>
</div>
<div id="buttonbar-page-global" class="buttonbar">
  <button id="push-updates">Push Updates</button>
</div>
</div>
<div id="page-edit" style="display:none">
  <div id="editor-content">
    <ul>
      <li><a href="#editor-panel">Edit</a></li>
      <li><a class="preview-button" href="#preview-panel">Preview</a></li>
    </ul>
    <div id="editor-panel">
      <div id="page-editor" class="wmd-panel">
        <div id="wmd-button-bar"></div>
        <textarea class="wmd-input" id="wmd-input">
${data.content}
</textarea>
      </div>
    </div>
    <div id="preview-panel">Preview goes here.</div>
  </div>
  <div id="buttonbar-editor-controls" class="buttonbar">
    <button id="save-edit">Save</button>
    <button id="cancel-edit">Cancel</button>
    <button id="delete-edit">Delete</button>
  </div>
</div>
<% } %>

<% if (!contentOnly){ %>
  <footer id="page-footer">
    <div class="generated-on">
      <span style="font-size: small;">Last updated: ${data.updated} by ${data.currentAuthor}.</span>
    </div>
    <div class="freeki-branding-help">
      <span style="font-size: 8pt; float: left;"><b>NOTE:</b> You can turn these off by editing \$HOME/freeki/.branding/static/css/branding.css and adding:
<pre>
.freeki-branding-help, .freeki-plug{display:none;}
</pre>
      </span>
    </div>
  </footer>
  
<% if ( !readOnly ){ %>
<!-- hidden panels -->
  <div id="page-edit-help" class="edit-help" style="display:none">Help goes here.<br/>Blat<br/>Blah.<br/>Boo</div>
  <div id="wmd-preview" class="wmd-preview" style="display:none"></div>
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
      init('/api/page/${data.id}', '/wiki/${data.group}/', '${data.group}', ${readOnly} );
    });
  </script>
  <script type="text/javascript" src="/static/js/branding.js"></script>
  <script type="text/javascript" src="/static/js/page-extras.js"></script>
 <% } %>
</body>
</html>