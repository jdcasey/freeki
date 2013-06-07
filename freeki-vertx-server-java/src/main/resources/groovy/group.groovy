# ${data.name}

<% data.children.each { %>
  - [${it.label}](${it.id}<% if (it.type.name().equals("GROUP")) { %>/<% } %>)
<% } %>
