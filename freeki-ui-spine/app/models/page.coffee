Spine = require('spine')

class Page extends Spine.Model
  @configure 'Page', 'serverPath', 'id', 'group', 'title', 'content', 'created', 'updated', 'currentAuthor'
  
  @extend Spine.Model.Ajax
  
  @url = '/freeki-data/pages/@'
  
  @filter: (query) ->
    return @all() unless query
    query = query.toLowerCase()
    @select (item) ->
      item.title?.toLowerCase().indexOf(query) isnt -1
        
  @setGroup: (group) ->
    @url = "/pages/#{group}/@"
  
module.exports = Page