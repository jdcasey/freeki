Spine = require('spine')
Page  = require('models/page')
$     = Spine.$

PagesDisplay = require('controllers/pages_display')
PagesIndex   = require('controllers/pages_index')

class Pages extends Spine.Controller
  className: 'pages'
  
  constructor: ->
    super
    
    @index = new PagesIndex
    @display = new PagesDisplay
    
    @routes
      '/pages/:id/edit': (params) ->
        @index.active(params)
        @display.edit.active(params)
      '/pages/:id': (params) ->
        @index.active(params)
        @display.display.active(params)
    
    @append @index, @display
    
    Page.fetch()
    
module.exports = Pages