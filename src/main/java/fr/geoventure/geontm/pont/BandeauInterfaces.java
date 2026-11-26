package fr.geoventure.geontm.pont;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.ContainerScreenEvent;

/**
 * Couche Geoventure des écrans principaux : un bandeau au-dessus de l'image d'origine, aux couleurs de la charte. Les
 * images NTM ne sont pas modifiées, pour garder les mises à jour de NTM-Continuum. Tout écran de machine {@code GUI...}
 * est habillé ({@link CategorieEcran}) ; le bandeau saute les écrans trop hauts pour le contenir.
 */
public final class BandeauInterfaces {

    private static final int HAUTEUR = 14;
    private static final int FOND = 0xFF232442;
    private static final int TEXTE = 0xFFFFFFFF;
    private static final int INDUSTRIE = 0xFF60A5FA;
    private static final int GUERRE = 0xFFF87171;

    private BandeauInterfaces() {}

    public static void dessiner(ContainerScreenEvent.Render.Background event) {
        AbstractContainerScreen<?> ecran = event.getContainerScreen();
        String nom = ecran.getClass().getName();
        CategorieEcran categorie = CategorieEcran.de(nom);
        if (categorie == CategorieEcran.AUCUNE || ecran.getTitle().getString().isBlank()) return;
        boolean guerre = categorie == CategorieEcran.GUERRE;
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
