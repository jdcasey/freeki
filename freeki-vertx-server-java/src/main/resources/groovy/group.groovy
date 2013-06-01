# ${data.name}

<% data.children.each { %>
  - [${it.label}](${it.id})
<% } %>
