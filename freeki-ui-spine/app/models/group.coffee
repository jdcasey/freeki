Spine = require('spine')

class Group extends Spine.Model
  @configure 'Group', 'serverPath', 'name', 'children'
  
  @extend Spine.Model.Ajax
  
  @url = '/freeki-data/groups/@'
  
  @filter: (query) ->
    groups = @all() unless query
    return groups if groups
    query = query.toLowerCase()
    @select (item) ->
      item.name?.toLowerCase().indexOf(query) isnt -1
    
  @fromJSON: (objects) ->
    return unless objects
    if typeof objects is 'string'
      objects = JSON.parse(objects)

    # Do some customization...
    
    objects = objects['items'] if objects['items']

    if Spine.isArray(objects)
      (console.log value['serverPath'] for value in objects)
      (new @(value) for value in objects)
    else
      new @(objects)
        
module.exports = Group