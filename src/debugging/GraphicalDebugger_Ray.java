package debugging;

import components.Graphics.GraphicsComponent;
import components.Graphics.Layer;
import physics.Ray;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GraphicalDebugger_Ray extends GraphicsComponent {

    public List<Ray> rays = new ArrayList<Ray>();

    public GraphicalDebugger_Ray() {
        super();
        this.layer = Layer.UI;
    }

    @Override
    public void Update() {
        rays.clear();
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(Color.magenta);
        Ray[] raysArray = new Ray[rays.size()];
        rays.toArray(raysArray);
        if (rays.size() > 0) {
            for (Ray ray : raysArray) {
                if (ray != null) {
                    graphics.drawLine((int) ray.origin.x, (int) ray.origin.y,
                            (int) (ray.origin.x + ray.direction.x * 50), (int) (ray.origin.y + ray.direction.y * 50));
                }
            }
        }
    }

}