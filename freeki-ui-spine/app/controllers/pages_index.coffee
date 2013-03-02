Spine = require('spine')
Page  = require('models/page')
List  = require('spine/lib/list')
$     = Spine.$

class PagesIndex extends Spine.Controller
  className: 'index'
  
  elements:
    '.items': 'items'
    'input[type=search]': 'search'
  
  events:
    'keyup input[type=search]': 'filter'
    'click footer button'     : 'create'
  
  constructor: ->
    super
    @html require('views/index')()
    
    @list = new List
      el: @items,
      template: require('views/item')
      selectFirst: true
    
    @list.bind 'change', @change
    
    @active (params) ->
      @list.change(Page.find(params.id))
    
    Page.bind('refresh change', @render)
  
  filter: ->
    @query = @search.val()
    @render()
  
  render: =>
    pages = Page.filter(@query)
    @list.render(pages)
  
  change: (item) =>
    @navigate '/pages', item.id
  
  create: ->
    item = Page.create()
    @navigate('/pages', item.id, 'edit')
    
module.exports = PagesIndex