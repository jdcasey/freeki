Spine = require('spine')
Group  = require('models/group')
List  = require('spine/lib/list')
$     = Spine.$

class GroupsSelector extends Spine.Controller
  className: 'selector'

  elements:
    '.selectorItems': 'items'
    'input[type=search]': 'search'

  events:
    'keyup input[type=search]': 'filter'
    'click footer button'     : 'create'

  constructor: ->
    super
    @html require('views/selector')()

    @list = new List
      el: @items,
      template: require('views/selector_item')
      selectFirst: true

    @list.bind 'change', @change

    @active (params) ->
      @list.change(Group.find(params.group))

    Group.bind('refresh change', @render)

  filter: ->
    @query = @search.val()
    @render()

  render: =>
    pages = Group.filter(@query)
    @list.render(pages)

  change: (item) =>
    alert("Click!")
    console.log($('.selectorItems').html())
    @navigate item.name

  create: ->
    item = Group.create()
    item.name = 'change-me'
    @navigate(item.name, 'edit')

module.exports = GroupsSelector