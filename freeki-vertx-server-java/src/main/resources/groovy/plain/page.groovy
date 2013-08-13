<% def last = '/wiki/' %>
[Wiki Root](${last}) /<% data.group.split('/').each { last = last + it + '/' %> [${it}](${last}) /<% } %>

${data.content}
