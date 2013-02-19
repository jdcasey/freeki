class Post < ActiveRecord::Base
  # extend FriendlyId
  attr_accessible :content, :name
  # friendly_id :name
end
