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
def last = '/wiki/'
def path = data.name.split('/')
def me = path.length > 0 ? path[path.length-1] : 'Wiki Root'
if ( path.length > 0 ){
  def newpath = new String[path.length-1]
  System.arraycopy(path,0,newpath,0,path.length-1)
  path = newpath
} %>
<% if(!data.name.equals("/")){ %>[Wiki Root](${last}) /<% path.each { last = last + it + '/' %> [${it}](${last}) /<% }} %>

# <% if (me == 'Wiki Root') { %>Wiki Root<% }else{ %>${data.name}<% } %>

<% if ( data.children ){ %> 
<% data.children.each { %>
  - **[${it.label}](${it.id}<% if (it.type.name().equals("GROUP")) { %>/<% } %>)**<% } %>
<% } %>
