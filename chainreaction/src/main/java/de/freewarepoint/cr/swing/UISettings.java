package de.freewarepoint.cr.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Player;
import de.freewarepoint.retrofont.RetroFont;

public class UISettings extends JPanel implements Runnable {

	private static final long serialVersionUID = -4840442627860470783L;

	private final UIGame uiGame;
	private Game game;                          // estado da partida

	private final JButton chooseAI1Button;
	private final JButton chooseAI2Button;
	private final RetroFont retroFont = new RetroFont();

	/* ---------------- construtor ---------------- */
	public UISettings(final UIGame uiGame) {
		super();
		this.uiGame = uiGame;

		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		setLayout(new BorderLayout(16, 16));

		/* --- LOGO ---------------------------------------------------------------- */
		JPanel logoPanel = new JPanel(new BorderLayout());
		logoPanel.setBackground(Color.BLACK);
		logoPanel.add(new JLabel(
				new ImageIcon(
						retroFont.getRetroString("ChainReaction", Color.WHITE, 32))));
		add(logoPanel, BorderLayout.WEST);

		/* --- BOTÕES -------------------------------------------------------------- */
		JPanel buttonPanel = new JPanel(
				new GridLayout(3, 2, 16, 16));          // 3 linhas × 2 colunas
		buttonPanel.setBackground(Color.BLACK);

		/* Player 1 --------------------------------------------------------------- */
		chooseAI1Button = createTextButton(
				"Player 1 (Human)",
				UIPlayer.getPlayer(Player.FIRST).getForeground(),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						uiGame.showChooseAI(Player.FIRST);
					}
				});
		buttonPanel.add(chooseAI1Button);

		/* New Game --------------------------------------------------------------- */
		buttonPanel.add(createTextButton(
				"New Game", Color.WHITE,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						uiGame.startNewGame();
					}
				}));

		/* Player 2 --------------------------------------------------------------- */
		chooseAI2Button = createTextButton(
				"Player 2 (Human)",
				UIPlayer.getPlayer(Player.SECOND).getForeground(),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						uiGame.showChooseAI(Player.SECOND);
					}
				});
		buttonPanel.add(chooseAI2Button);

		/* Exit ------------------------------------------------------------------- */
		buttonPanel.add(createTextButton(
				"Exit", Color.WHITE,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						uiGame.dispatchEvent(
								new WindowEvent(uiGame, WindowEvent.WINDOW_CLOSING));
					}
				}));

		/* Save Game -------------------------------------------------------------- */
		buttonPanel.add(createTextButton(
				"Save Game", Color.WHITE,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						uiGame.saveGameDialog();
					}
				}));

		/* Load Game -------------------------------------------------------------- */
		buttonPanel.add(createTextButton(
				"Load Game", Color.WHITE,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						uiGame.loadGameDialog();
					}
				}));

		add(buttonPanel, BorderLayout.CENTER);
	}

	/* ---------------- setter chamado pelo UIGame ---------------- */
	public void setGame(Game game) { this.game = game; }

	/* ---------------- Runnable p/ SwingUtilities.invokeLater ----- */
	@Override
	public void run() {
		if (game != null) {
			chooseName(Player.FIRST, chooseAI1Button);
			chooseName(Player.SECOND, chooseAI2Button);
		}
	}

	/* ---------------- helpers privados --------------------------- */
	private JButton createTextButton(
			String label, Color fg, ActionListener al) {

		JButton b = new JButton();
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);

		ImageIcon normal  = new ImageIcon(
				retroFont.getRetroString(label, fg, 16));
		ImageIcon pressed = new ImageIcon(
				retroFont.getRetroString(label, Color.BLACK, fg, 16));

		b.setIcon(normal);
		b.setPressedIcon(pressed);
		b.addActionListener(al);
		return b;
	}

	private void chooseName(final Player player, JButton button) {
		String name = game.getPlayerStatus(player).isAIPlayer()
				? game.getPlayerStatus(player).getAI().getName()
				: "Human";
		String str = "Player " + (player.ordinal() + 1) + " (" + name + ")";
		button.setIcon(new ImageIcon(
				retroFont.getRetroString(
						str, UIPlayer.getPlayer(player).getForeground(), 16)));
		button.setPressedIcon(new ImageIcon(
				retroFont.getRetroString(
						str, Color.BLACK, UIPlayer.getPlayer(player).getForeground(), 16)));
	}
}
