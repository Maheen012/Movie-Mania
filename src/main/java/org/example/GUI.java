package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }

    public GUI() {
        JFrame frame = new JFrame("Movie Mania - Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Movie Mania", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        JButton btnSearch = new JButton("Search Movies");
        JButton btnViewFavorites = new JButton("View Favorites");
        JButton btnWatchHistory = new JButton("Watch History");
        JButton btnLogout = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnViewFavorites);
        buttonPanel.add(btnWatchHistory);
        buttonPanel.add(btnLogout);
        frame.add(buttonPanel, BorderLayout.CENTER);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Search Movies clicked");
            }
        });

        btnViewFavorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "View Favorites clicked");
            }
        });

        btnWatchHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWatchHistoryScreen();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Logged Out");
            }
        });

        frame.setVisible(true);
    }

    private void showWatchHistoryScreen() {
        JFrame watchHistoryFrame = new JFrame("Watch History");
        watchHistoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        watchHistoryFrame.setSize(400, 300);
        watchHistoryFrame.setLayout(new BorderLayout());

        JLabel lblWatchHistory = new JLabel("Your Watch History", JLabel.CENTER);
        lblWatchHistory.setFont(new Font("Arial", Font.BOLD, 24));
        watchHistoryFrame.add(lblWatchHistory, BorderLayout.NORTH);

        JButton btnBack = new JButton("Back to Catalog");
        watchHistoryFrame.add(btnBack, BorderLayout.CENTER);

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                watchHistoryFrame.dispose();
                new GUI();
            }
        });

        watchHistoryFrame.setVisible(true);
    }
}
