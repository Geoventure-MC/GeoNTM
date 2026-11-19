package fr.geoventure.geontm.pont;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.ContainerScreenEvent;

import java.util.Set;

/**
 * Couche Geoventure des écrans principaux : un bandeau au-dessus de l'image d'origine, aux couleurs de la charte. Les
 * images NTM ne sont pas modifiées, pour garder les mises à jour de NTM-Continuum.
 */
public final class BandeauInterfaces {

    private static final int HAUTEUR = 14;
    private static final int FOND = 0xFF232442;
    private static final int TEXTE = 0xFFFFFFFF;
    private static final int INDUSTRIE = 0xFF60A5FA;
    private static final int GUERRE = 0xFFF87171;

    private static final Set<String> ECRANS_INDUSTRIE = Set.of("GUIMachineAssemblyMachine", "GUIMachineAssemblyFactory",
            "GUIMachineChemicalPlant", "GUIMachineChemicalFactory", "GUIMachineArcWelder", "GUIMachinePress",
            "GUIMachineElectricFurnace", "GUIMachineBattery", "GUIMachinePUREX", "GUIPWR");
    private static final Set<String> ECRANS_GUERRE = Set.of("GUILaunchPad", "GUIMachineMissileAssembly",
            "GUIMachineRadarNT", "GUIMachineRadarNTSlots");

    private BandeauInterfaces() {}

    public static void dessiner(ContainerScreenEvent.Render.Background event) {
        AbstractContainerScreen<?> ecran = event.getContainerScreen();
        String nom = ecran.getClass().getSimpleName();
        boolean guerre = ECRANS_GUERRE.contains(nom);
        if (!guerre && !ECRANS_INDUSTRIE.contains(nom)) return;
        int x = ecran.getGuiLeft();
        int y = ecran.getGuiTop() - HAUTEUR;
        if (y < 0) return;

        GuiGraphics g = event.getGuiGraphics();
        int largeur = ecran.getXSize();
        g.fill(x, y, x + largeur, y + HAUTEUR, FOND);
        g.fill(x, y, x + 3, y + HAUTEUR, guerre ? GUERRE : INDUSTRIE);
        Font police = Minecraft.getInstance().font;
        String titre = police.plainSubstrByWidth(ecran.getTitle().getString(), largeur - 12);
        g.drawString(police, titre, x + 8, y + 3, TEXTE, false);
    }
}
