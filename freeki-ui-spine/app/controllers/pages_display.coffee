Spine = require('spine')
Page  = require('models/page')
Markdown = require('pagedown/node-pagedown')
$     = Spine.$

class Display extends Spine.Controller
  className: 'show'
    
  events:
    'click .edit': 'edit'
  
  constructor: ->
    super
    @markdown = new Markdown.Converter
    @active @change
  
  render: ->
    @html require('views/display')(@)
  
  change: (params) =>
    @item = Page.find(params.id)
    @render()
  
  edit: ->
    @navigate('/pages', @item.id, 'edit')


class Edit extends Spine.Controller
  className: 'edit'
  
  events:
    'submit form': 'submit'
    'click .save': 'submit'
    'click .delete': 'delete'
  
  elements:
    'form': 'form'
  
  constructor: ->
    super
    @active @change
  
  render: ->
    @html require('views/form')(@item)
  
  change: (params) =>
    @item = Page.find(params.id)
    @render()
  
  submit: (e) =>
    e.preventDefault()
    @item.fromForm(@form).save()
    @navigate('/pages', @item.id)
  
  delete: ->
    @item.destroy() if confirm('Are you sure?') and confim('This is forever. REALLY??')


class PagesDisplay extends Spine.Stack
  controllers:
    display: Display
    edit:    Edit

module.exports = PagesDisplay