// GeoCore KubeJS adapter smoke check (dev runs with -Pkubejs.run=true only). Positive signals, in logs/kubejs/startup.log:
// whether HBM's event group and binding reached the startup scripts (they do only if GEONTM was constructed before KubeJS).
console.info(`[geocore-kubejs] startup: GEONTMEvents ${typeof GEONTMEvents}, GEONTM ${typeof GEONTM}`)
if (typeof GEONTMEvents !== 'undefined') console.info(`[geocore-kubejs] startup: GEONTMEvents events ${Object.keys(GEONTMEvents)}`)
// The startup event: posted once after the startup scripts; the event object is the payload (properties and methods).
if (typeof GEONTMEvents !== 'undefined') GEONTMEvents.registry(event => console.info(`[geocore-kubejs] startup: GEONTMEvents.registry posted, keys ${Object.keys(event)}`))
