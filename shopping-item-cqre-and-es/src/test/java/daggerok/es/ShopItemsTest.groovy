package daggerok.es

import spock.lang.Specification

class ShopItemsTest extends Specification {

  UUID id = UUID.randomUUID()
  ShopItems shopItem = new ShopItems(id)

  def 'should create shop item'() {
    given:
      UUID id = UUID.randomUUID()
    when:
      ShopItems shopItem = new ShopItems(id)
    then:
      shopItem.id != null
    and:
      shopItem.id == id
  }
}
