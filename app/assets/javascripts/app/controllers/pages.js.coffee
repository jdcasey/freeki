#$ = jQuery.sub()
Page = App.Page

$.fn.item = ->
  elementID   = $(@).data('id')
  elementID or= $(@).parents('[data-id]').data('id')
  Page.find(elementID)

class New extends Spine.Controller
  events:
    'click [data-type=back]': 'back'
    'submit form': 'submit'
    
  constructor: ->
    super
    @active @render
    
  render: ->
    @html @view('pages/new')

  back: ->
    @navigate '/pages'

  submit: (e) ->
    e.preventDefault()
    
    page = Page.fromForm(e.target)
    page.path = page.title.toLowerCase().replace(new RegExp(' +', 'g'), '-')
    
    if confirm( page.path )
      page = page.save()
    
      @navigate page.path if page

class Edit extends Spine.Controller
  events:
    'click [data-type=back]': 'back'
    'submit form': 'submit'
  
  constructor: ->
    super
    @active (params) ->
      @change(params.path)
      
  change: (path) ->
    @item = Page.findByAttribute('path', path)
    @render()
    
  render: ->
    @html @view('pages/edit')(@item)

  back: ->
    @navigate '/pages'

  submit: (e) ->
    e.preventDefault()
    @item.fromForm(e.target).save()
    @navigate '/pages'

class Show extends Spine.Controller
  events:
    'click [data-type=edit]': 'edit'
    'click [data-type=back]': 'back'

  constructor: ->
    super
    @markdown = new Markdown.Converter
    
    @active (params) ->
      @change(params.path)

  change: (path) ->
    @item = Page.findByAttribute('path', path)
    alert( @item )
    @render()

  render: ->
    @html @view('pages/show')(@)

  edit: ->
    @navigate '/pages', @item.path, 'edit'

  back: ->
    @navigate '/pages'

class Index extends Spine.Controller
  events:
    'click [data-type=edit]':    'edit'
    'click [data-type=destroy]': 'destroy'
    'click [data-type=show]':    'show'
    'click [data-type=new]':     'new'

  constructor: ->
    super
    Page.bind 'refresh change', @render
    Page.fetch()
    
  render: =>
    pages = Page.all()
    @html @view('pages/index')(pages: pages)
    
  edit: (e) ->
    item = $(e.target).item()
    @navigate '/pages', item.path, 'edit'
    
  destroy: (e) ->
    item = $(e.target).item()
    item.destroy() if confirm('Sure?')
    
  show: (e) ->
    item = $(e.target).item()
    alert( '/pages/' + item.path )
    @navigate '/pages', item.path
    
  new: ->
    @navigate '/pages/new'
    
class App.Pages extends Spine.Stack
  controllers:
    index: Index
    edit:  Edit
    show:  Show
    new:   New
    
  routes:
    '/pages/new':        'new'
    '/pages/:path/edit': 'edit'
    '/pages/:path':      'show'
    '/pages':        'index'
    
  default: 'index'
  className: 'stack pages'