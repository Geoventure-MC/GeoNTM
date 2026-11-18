// GeoCore KubeJS adapter smoke check (dev runs with -Pkubejs.run=true only). Positive signals, in logs/kubejs/server.log:
// whether KubeJS has GEONTM recipe table schemas, and whether the GEONTM script binding exists.
ServerEvents.recipes(event => {
  console.info(`[geocore-kubejs] event.recipes.geontm.press: ${typeof event.recipes.geontm.press}`)
  console.info(`[geocore-kubejs] GEONTM binding: ${typeof GEONTM}`)
})
