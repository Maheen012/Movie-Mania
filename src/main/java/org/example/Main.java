package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        // Create and show the GUI
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        // Frame setup
        JFrame frame = new JFrame("Movie Mania - Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Header label
        JLabel lblTitle = new JLabel("Movie Mania", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        // Create buttons
        JButton btnSearch = new JButton("Search Movies");
        JButton btnViewFavorites = new JButton("View Favorites");
        JButton btnWatchHistory = new JButton("Watch History");
        JButton btnLogout = new JButton("Logout");

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnViewFavorites);
        buttonPanel.add(btnWatchHistory);
        buttonPanel.add(btnLogout);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Action listeners for buttons
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action for search (You can customize this)
                JOptionPane.showMessageDialog(frame, "Search Movies clicked");
            }
        });

        btnViewFavorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action for View Favorites
                JOptionPane.showMessageDialog(frame, "View Favorites clicked");
            }
        });

        btnWatchHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action for Watch History (Switch to Watch History screen)
                showWatchHistoryScreen();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action for Logout
                JOptionPane.showMessageDialog(frame, "Logged Out");
                // Handle logout action, like redirect to login screen
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    // Method to show Watch History screen
    private void showWatchHistoryScreen() {
        JFrame watchHistoryFrame = new JFrame("Watch History");
        watchHistoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        watchHistoryFrame.setSize(400, 300);
        watchHistoryFrame.setLayout(new BorderLayout());

        // Header label
        JLabel lblWatchHistory = new JLabel("Your Watch History", JLabel.CENTER);
        lblWatchHistory.setFont(new Font("Arial", Font.BOLD, 24));
        watchHistoryFrame.add(lblWatchHistory, BorderLayout.NORTH);

        // Button to go back to Catalog
        JButton btnBack = new JButton("Back to Catalog");
        watchHistoryFrame.add(btnBack, BorderLayout.CENTER);

        // Action listener to handle back button
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                watchHistoryFrame.dispose();  // Close Watch History frame
                new Main();  // Open Movie Catalog screen again
            }
        });

        // Display the Watch History screen
        watchHistoryFrame.setVisible(true);
    }
}
