package observer.pattern;

import observer.CourseRecord;
import observer.LayoutConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class PieChartObserver extends JPanel implements Observer {
    /**
     * Creates a BarChartObserver object
     *
     * @param data
     *            a CourseData object to observe
     */
    public PieChartObserver(CourseData data) {
        data.attach(this);
        this.courseData = data.getUpdate();
        this.setPreferredSize(new Dimension(2 * LayoutConstants.xOffset
                + (LayoutConstants.barSpacing + LayoutConstants.barWidth)
                * this.courseData.size(), LayoutConstants.graphHeight + 2
                * LayoutConstants.yOffset));
        this.setBackground(Color.white);
    }

    /**
     * Paint method
     *
     * @param g
     *            a Graphics object on which to paint
     */
    public void paint(Graphics g) {
        super.paint(g);
        //pie chart
        int radius = 100;
        double total = 0.0;
        for (int i = 0; i < courseData.size(); i++) {
            total += courseData.elementAt(i).getNumOfStudents();
        }
        if (total != 0) {
            double startAngle = 0.0;
            for (int i = 0; i < courseData.size(); i++) {
                double ratio = (courseData.elementAt(i).getNumOfStudents() / total) * 360.0;
                //draw the arc
                g.setColor(LayoutConstants.courseColours[i%LayoutConstants.courseColours.length]);
                g.fillArc(LayoutConstants.xOffset, LayoutConstants.yOffset, 2 * radius, 2 * radius, (int) startAngle, (int) ratio);
                startAngle += ratio;
            }
        }
    }

    /**
     * Informs this observer that the observed CourseData object has changed
     *
     * @param o
     *            the observed CourseData object that has changed
     */
    public void update(Observable o) {
        CourseData data = (CourseData) o;
        this.courseData = data.getUpdate();

        this.setPreferredSize(new Dimension(2 * LayoutConstants.xOffset
                + (LayoutConstants.barSpacing + LayoutConstants.barWidth)
                * this.courseData.size(), LayoutConstants.graphHeight + 2
                * LayoutConstants.yOffset));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void update(Object o) {
        CourseRecord record = (CourseRecord) o;

        boolean contain = false;
        for (CourseRecord courseRecord : courseData) {
            if (courseRecord.getName().equals(record.getName())) {
                courseRecord.setNumOfStudents(record.getNumOfStudents());
                contain = true;
            }
        }

        if (!contain)
            courseData.add(record);

        this.revalidate();
        this.repaint();
    }

    private Vector<CourseRecord> courseData;
}