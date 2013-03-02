require('lib/setup')

Spine = require('spine')
Pages  = require('controllers/pages')

class App extends Spine.Controller
  constructor: ->
    super
    @pages = new Pages
    @append @pages
    
    Spine.Route.setup()

module.exports = App
    