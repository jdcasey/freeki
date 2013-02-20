class CreatePages < ActiveRecord::Migration
  def change
    create_table :pages do |t|
      t.string :title
      t.string :path
      t.string :content

      t.timestamps
    end
  end
end
