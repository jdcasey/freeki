Spine = require('spine')

class Page extends Spine.Model
  @configure 'Page', 'path', 'title', 'content', 'created', 'updated', 'currentAuthor'
  
  @extend @Local
  
  @filter: (query) ->
    return @all() unless query
    query = query.toLowerCase()
    @select (item) ->
      item.title?.toLowerCase().indexOf(query) isnt -1 or
        item.path?.toLowerCase().indexOf(query) isnt -1
  
module.exports = Page