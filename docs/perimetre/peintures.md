# Peintures de fuselages de missile

Recettes d'établi ajoutées par Geoventure dans `GeoventureCraftingRecipes` (classe A de `obtention-audit.tsv`, famille « peintures de missile »). Ni NTM-Continuum ni HBM 1.7.10 ne savaient fabriquer ces variantes (créatif seulement).

- **Peindre** : fuselage de base de même taille et même carburant + 1 colorant vanilla (sans forme) donne la variante (`<variante>_paint`).
- **Laver** : variante + 1 seau d'eau rend le fuselage de base, le seau revient vide (`<variante>_wash`).
- Le même motif prend le même colorant sur toutes les tailles. Au sein d'un fuselage de base, deux variantes n'ont jamais le même colorant (vérifié au chargement : une exception est levée sinon).
- Les variantes isolation, métal et desh avaient déjà leur recette d'origine ; elles ne sont pas touchées.

| Motif | Colorant | Motif | Colorant |
|---|---|---|---|
| camo | vert | soviet_glory | rouge |
| desert | jaune | soviet_stank | marron |
| flames | orange | battery | vert clair |
| sky | bleu clair | cathedral | cyan |
| sleek | noir | duracell | marron |
| taint | violet | moonlit | bleu |
| dash | blanc | magnusson | bleu |
| vap | magenta | blackjack | noir |
| bullet | gris | decorated | rose |
| silvermoonlight | gris clair | lambda | orange |
| minuteman | bleu | pip | vert clair |
| polite | blanc | steampunk | gris |
| yuck | marron | faust | noir |
| mimi | rose | panorama | bleu clair |
| roses | magenta | snowy | blanc |

Fuselages de base concernés : `mp_fuselage_10_kerosene`, `10_long_kerosene`, `10_solid`, `10_long_solid`, `15_kerosene`, `15_solid`, `15_hydrogen`, `15_20_kerosene` (48 variantes, 96 recettes).

## Restent cachés

- `missile_skin_*` (9 objets) : aucun usage dans le code (pas de recette, pas d'assemblage, pas de rendu ; simple icône, comme en 1.7). Ids gardés, classe B.
- `mp_warhead_15_nuclear_mimi` et `mp_warhead_15_nuclear_shark` : variantes de peinture d'ogive, hors du périmètre de cette décision (fuselages seulement). Classe B.
- `mp_fuselage_10_xenon_bhole` et `mp_fuselage_15_balefire` : arme extrême, sans recette par décision du propriétaire. Classe B.
