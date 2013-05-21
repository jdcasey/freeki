Spine = require('spine')
Group = require('models/group')
$     = Spine.$

GroupsSelector = require('controllers/groups_selector')
PagesIndex   = require('controllers/pages_index')

class Groups extends Spine.Controller
  className: 'groups'
  
  constructor: (pagesIndex) ->
    super
    
    @selector = new GroupsSelector
    @pagesIndex = pagesIndex
    
    @active (params) ->
      alert(JSON.stringify(params))
      
    @routes
      '/:group': (params) ->
        @selector.active(params)
        @pagesIndex.active(params) if @pagesIndex
      '/:group/edit': (params) ->
        @selector.active(params)
        @pagesIndex.deactivate() if @pagesIndex
    
    @append @selector
    
    console.log("fetching groups")
    Group.fetch()
  
module.exports = Groups