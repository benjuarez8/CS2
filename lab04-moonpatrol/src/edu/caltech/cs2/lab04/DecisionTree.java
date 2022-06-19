package edu.caltech.cs2.lab04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DecisionTree {
    private final DecisionTreeNode root;

    public DecisionTree(DecisionTreeNode root) {
        this.root = root;
    }

    public String predict(Dataset.Datapoint point) {
        return predict_helper(point, this.root);
    }

    private String predict_helper(Dataset.Datapoint point, DecisionTreeNode root) {
        if (root.isLeaf()) {
            OutcomeNode outcome_node = (OutcomeNode) root;
            return outcome_node.outcome;
        }
        else {
            AttributeNode att_node = (AttributeNode) root;
            String attribute = att_node.attribute;
            String feature = point.attributes.get(attribute);
            DecisionTreeNode dt_node = att_node.children.get(feature);
            return predict_helper(point, dt_node);
        }
    }

    public static DecisionTree id3(Dataset dataset, List<String> attributes) {
        System.out.print(dataset.pointsHaveSameOutcome());
        if (dataset.pointsHaveSameOutcome() != "") {  // checks if remaining data points have same outcome
            DecisionTreeNode outcome_node = new OutcomeNode(dataset.pointsHaveSameOutcome());
            DecisionTree dt = new DecisionTree(outcome_node);
            return dt; // returns decision tree with outcome node with this outcome
        }
        if (attributes.size() == 0) { // checks if there are remaining attributes
            DecisionTreeNode outcome_node = new OutcomeNode(dataset.getMostCommonOutcome());
            DecisionTree dt = new DecisionTree(outcome_node);
            return dt; // returns decision tree with outcome node with most common outcome
        }
        String a = dataset.getAttributeWithMinEntropy(attributes); // lowest entropy attribute
        HashMap<String, DecisionTreeNode> m = new HashMap<>();
        for (String f : dataset.getFeaturesForAttribute(a)) { // loops through each feature of a
            Dataset new_data = dataset.getPointsWithFeature(f); // finds data points with feature f
            if (new_data.size() == 0) {
                // makes outcome child with most common outcome if no such data points exist
                DecisionTreeNode outcome_child = new OutcomeNode(dataset.getMostCommonOutcome());
                m.put(f, outcome_child);
            }
            else { // recursively makes child out of remaining data_points using all attributes (except for a)
                List<String> new_attributes = new ArrayList<>();
                for (String s : attributes) {
                    if (!s.equals(a)) {
                        new_attributes.add(s);
                    }
                }
                DecisionTree dt = id3(new_data, new_attributes);
                m.put(f, dt.root);
            }
        }
        DecisionTreeNode a_node = new AttributeNode(a, m);
        DecisionTree dt = new DecisionTree(a_node);
        return dt; // returns decision tree with attribute node using children generated above
    }
}
