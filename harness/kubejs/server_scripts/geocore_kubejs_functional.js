// KubeJS functional check for the harness scenario KubeJsSupport (dev runs with -Pkubejs.run=true only). The same file
// runs on every KubeJS node (8.x, 7.x, 6.x): it uses only syntax the GeoCore adapter promises on all three.
// Positive signals: recipe ids the client receives in GeoCore's recipe-table sync, and "[kjs] ..." chat lines.

ServerEvents.recipes(event => {
  // typed positional arguments: output item, input ingredient, stamp
  event.recipes.geontm.press('minecraft:diamond', 'minecraft:coal_block', 'flat').id('kubejs:press_typed')
  // a counted output and a tag input
  event.recipes.geontm.shredder('2x minecraft:gravel', '#minecraft:logs').id('kubejs:shredder_tag')
  // a plain object passes through unchanged (HBM's own ingredient form)
  event.recipes.geontm.press('minecraft:emerald', {ingredient: 'minecraft:gold_block', count: 2}, 'flat').id('kubejs:press_raw')
  // a generic table: required name first, the rest by builder methods (camelCase aliases), a fluid output
  event.recipes.geontm.chemical_plant('kjs.test')
    .inputItems(['2x minecraft:iron_ingot'])
    .outputFluids([Fluid.of('geontm:oxygen', 500)])
    .duration(40)
    .power(100)
    .id('kubejs:chem_typed')
  // the same table with optional fields passed positionally (name, outputItems, inputItems, outputFluids, inputFluids,
  // duration, power): scripts written against the full signature keep working
  event.recipes.geontm.chemical_plant('kjs.positional', ['minecraft:diamond'], ['minecraft:coal'], [], [], 40, 100)
    .id('kubejs:chem_positional')
  // an output with item data (components from 1.20.5, NBT on 1.20.1) keeps it: GameTest script_fields_output_item_data
  const damaged = Item.of('minecraft:iron_pickaxe')
  damaged.setDamageValue(3)
  event.recipes.geontm.assembly_machine('kjs.item_data')
    .inputItems(['minecraft:iron_ingot'])
    .outputItems([damaged])
    .duration(20)
    .power(10)
    .id('kubejs:assembly_item_data')
  // removal by id, and by type + output (Shape.read answers the plain output form)
  event.remove({id: 'geontm:press/ball_resin'})
  event.remove({type: 'geontm:press', output: 'geontm:biomass_compressed'})
})

// observe-only events: report to chat so the client log shows them
GEONTMEvents.explosion(event => {
  event.level.server.runCommandSilent(`say [kjs] explosion ${event.kind} size ${event.size}`)
})
GEONTMEvents.entityIrradiated(event => {
  event.entity.level.server.runCommandSilent(`say [kjs] irradiated ${event.hazard} dose>0 ${event.dose > 0}`)
})

// the GEONTM binding, driven from chat
const ExplosionVNT = Java.loadClass('fr.geoventure.geontm.explosion.vanillant.ExplosionVNT')
PlayerEvents.chat(event => {
  const msg = event.message
  const player = event.player
  const server = player.level.server
  if (msg == 'kjs binding') {
    // On the server thread: KubeJS 6 posts chat events off it (Architectury's chat hook), and chunk radiation reads
    // only loaded chunks, which vanilla answers on the server thread alone.
    server.execute(() => {
      GEONTM.setRadiation(player, 123)
      const fluid = GEONTM.fluid('geontm:oxygen')
      const iron = GEONTM.material('iron')
      GEONTM.setChunkRadiation(player.level, player.blockPosition(), 77)
      server.runCommandSilent(`say [kjs] rad ${Math.round(GEONTM.getRadiation(player))} fluid ${fluid ? fluid.id : 'none'} iron ${iron ? 'yes' : 'no'} chunk ${Math.round(GEONTM.getChunkRadiation(player.level, player.blockPosition()))}`)
    })
  }
  if (msg == 'kjs material') {
    // No block-level declarations here: Rhino scopes them to the whole handler function.
    server.runCommandSilent(`say [kjs] material ${GEONTM.material('kubejs_testium') ? GEONTM.material('kubejs_testium').name : 'none'}`)
  }
  if (msg == 'kjs boom') {
    server.execute(() => new ExplosionVNT(player.level, player.x + 8, player.y, player.z, 2.0).makeStandard().explode())
  }
  if (msg == 'kjs irradiate') {
    server.execute(() => {
      const pig = player.level.createEntity('minecraft:pig')
      pig.setPosition(player.x + 3, player.y, player.z)
      pig.spawn()
      GEONTM.contaminate(pig, 'RADIATION', 'NONE', 10)
    })
  }
})
