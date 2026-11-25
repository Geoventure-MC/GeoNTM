#!/bin/bash
# Banc des GameTests de GeoNTM (Forge 1.20.1) : bash tools/run_gametests.sh [-Pgametest.filter=<regex>] [autres options gradle]
#
# - Le serveur de test n'a ni GeoMods ni Ad Astra : -Pgeontm.bench=true les rend facultatifs dans les ressources du banc
#   seulement (voir build.forge.gradle.kts), le jar livré ne change pas.
# - Sans filtre explicite, les tests listés dans docs/perimetre/gametests-ecartes.tsv (avec classe et raison) ne sont pas lancés.
# - Une seule construction Gradle à la fois : verrou GRADLE_LOCK (par défaut /c/Users/bmeouchi/dl-tools/gradle.lock).
# - JDK de Gradle : GRADLE_JAVA_HOME (par défaut le JDK 21 de bmeouchi).
# - Code de sortie 0 seulement si la suite est allée au bout sans échec hors liste.
cd "$(dirname "$0")/.." || exit 2
export JAVA_HOME="${GRADLE_JAVA_HOME:-/c/Users/bmeouchi/tools/jdk/jdk-21.0.13.1+2}"  # Gradle exige un JDK 21 ; les runs de test lancent leur propre Java 17
export PATH="$JAVA_HOME/bin:$PATH" JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS:--Dfile.encoding=UTF-8}"
LOCK="${GRADLE_LOCK:-/c/Users/bmeouchi/dl-tools/gradle.lock}"
LOG="${GAMETEST_LOG:-versions/1.20.1-forge/runs/gametest-gradle.log}"
mkdir -p "$(dirname "$LOG")"

while ! mkdir "$LOCK" 2>/dev/null; do sleep 20; done
trap 'rmdir "$LOCK" 2>/dev/null' EXIT
./gradlew :1.20.1-forge:runGameTestServer -Pgeontm.bench=true --offline --console=plain "$@" > "$LOG" 2>&1
echo "gradle rc=$?"

RUN=versions/1.20.1-forge/runs/gametest/logs/latest.log
if ! grep -q "GAME TESTS COMPLETE" "$LOG"; then
    echo "ECHEC DU BANC : la suite n'est pas allée au bout (voir $LOG)"
    exit 1
fi
grep "GAME TESTS COMPLETE\|required tests" "$LOG" | sed 's/.*GameTestServer\]: //'
sed -n 's/.*LogTestReporter[^:]*: \(geontm:[^ ]*\) failed!.*/\1/p' "$RUN" | sort -u > "$LOG.echecs"
grep -v '^#' docs/perimetre/gametests-ecartes.tsv | cut -f1 | sed 's/^/geontm:/' | sort -u > "$LOG.ecartes"
# un test écarté ne tourne pas ; s'il apparaît quand même (filtre explicite), il reste expliqué par le fichier
comm -23 "$LOG.echecs" "$LOG.ecartes" > "$LOG.inexpliques"
echo "échecs : $(wc -l < "$LOG.echecs"), dont non expliqués : $(wc -l < "$LOG.inexpliques")"
cat "$LOG.inexpliques"
[ ! -s "$LOG.inexpliques" ]
