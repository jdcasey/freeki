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
</head>
<body>
<header id="header"></header>
<div id="breadcrumbs">
<% if(!data.name.equals("/")){ %>[Wiki Root](${last}) /<% path.each { last = last + it + '/' %> [${it}](${last}) /<% }} %>
</div>

# <% if (me == 'Wiki Root') { %>Wiki Root<% }else{ %>${data.name}<% } %>

<% if ( data.children ){ %> 
<% data.children.each { %>
  - **[${it.label}](${it.id}<% if (it.type.name().equals("GROUP")) { %>/<% } %>)**<% } %>
<% } %>

<footer><span style="font-size: small; float: center;">
Generated on ${new Date()}.
</span></footer>
</body>
</html>