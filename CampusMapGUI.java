import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;

public class CampusMapGUI {
    private UUMcampus uumCampus;
    private JComboBox<Node> sourceNodeComboBox;
    private JComboBox<Node> destinationNodeComboBox;
    private JTextArea resultTextArea;

    public CampusMapGUI(UUMcampus uumCampus) {
        this.uumCampus = uumCampus;
    }

    public void displayGUI() {
        JFrame frame = new JFrame("UUM Campus Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel sourceLabel = new JLabel("Source Node:");
        sourceNodeComboBox = new JComboBox<>(uumCampus.getNodes().toArray(new Node[0]));

        JLabel destinationLabel = new JLabel("Destination Node:");
        destinationNodeComboBox = new JComboBox<>(uumCampus.getNodes().toArray(new Node[0]));

        JButton findPathButton = new JButton("Find Shortest Path");
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);

        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Node sourceNode = (Node) sourceNodeComboBox.getSelectedItem();
                Node destinationNode = (Node) destinationNodeComboBox.getSelectedItem();

                if (sourceNode != null && destinationNode != null) {
                    List<Node> shortestPath = uumCampus.findShortestPath(sourceNode, destinationNode);
                    displayResult(shortestPath);
                    openGoogleMapsInBrowser(shortestPath);
                }
            }
        });

        panel.add(sourceLabel);
        panel.add(sourceNodeComboBox);
        panel.add(destinationLabel);
        panel.add(destinationNodeComboBox);
        panel.add(findPathButton);
        panel.add(new JLabel());
        panel.add(resultTextArea);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void displayResult(List<Node> shortestPath) {
        if (shortestPath != null) {
            double totalDistance = calculateTotalDistance(shortestPath);
            StringBuilder result = new StringBuilder("Shortest Path: ");
            for (Node node : shortestPath) {
                result.append(node.getName()).append(" -> ");
            }
            result.delete(result.length() - 4, result.length()); 
            result.append(String.format("\nTotal Distance: %.3f km", totalDistance));
            resultTextArea.setText(result.toString());
        } else {
            resultTextArea.setText("No path found.");
        }
    }

    private double calculateTotalDistance(List<Node> path) {
        double totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            totalDistance += current.calcDistance(next);
        }
        return totalDistance;
    }

    private void openGoogleMapsInBrowser(List<Node> path) {
        if (path != null && !path.isEmpty()) {
            Node sourceNode = path.get(0);
            Node destinationNode = path.get(path.size() - 1);

            String googleMapsUrl = "https://www.google.com/maps/dir/" +
                    sourceNode.getLatitude() + "," + sourceNode.getLongtitude() + "/" +
                    destinationNode.getLatitude() + "," + destinationNode.getLongtitude();

            try {
                Desktop.getDesktop().browse(new URI(googleMapsUrl));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
