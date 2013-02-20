class Page < ActiveRecord::Base
  extend FriendlyId
  
  attr_accessible :content, :title, :path
  
  friendly_id :path
end
