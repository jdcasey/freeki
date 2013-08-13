<html>
  <head><title>${data.title}</title></head>
  <body>
<% def last = '/wiki/' %>
[Wiki Root](${last}) /<% data.group.split('/').each { last = last + it + '/' %> [${it}](${last}) /<% } %>

${data.content}


<span style="font-size: small; float: center;">
Created on ${data.created}. Last updated: ${data.updated} by ${data.currentAuthor}.
</span>
</body>
</html>