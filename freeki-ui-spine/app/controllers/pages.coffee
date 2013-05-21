Spine = require('spine')
Page  = require('models/page')
$     = Spine.$

PagesDisplay = require('controllers/pages_display')
PagesIndex   = require('controllers/pages_index')

class Pages extends Spine.Controller
  className: 'pages'
  
  constructor: (pagesIndex) ->
    super
    
    @index = pagesIndex
    @display = new PagesDisplay
    
    @routes
      '/:group/:id/edit': (params) ->
        @index.active(params)
        @display.edit.active(params)
      '/:group/:id': (params) ->
        @index.active(params)
        @display.display.active(params)
    
    @append @display
    
module.exports = Pages