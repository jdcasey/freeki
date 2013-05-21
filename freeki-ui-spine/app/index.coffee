require('lib/setup')

Spine = require('spine')
Groups = require('controllers/groups')
Pages  = require('controllers/pages')
PagesIndex  = require('controllers/pages_index')

$      = Spine.$

class App extends Spine.Controller
  
  constructor: ->
    super
    
    @pagesIndex = new PagesIndex
    @pages = new Pages(@pagesIndex)
    @groups = new Groups(@pagesIndex)
    
    @append @groups, @pagesIndex, @pages
    
    Spine.Route.setup()
    @groups.activate()
    
module.exports = App
    