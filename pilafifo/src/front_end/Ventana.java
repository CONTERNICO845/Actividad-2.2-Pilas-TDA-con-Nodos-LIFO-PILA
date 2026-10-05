package front_end;

import back_end.Libro;
import back_end.Pila;
import back_end.Pila.PilaVaciaException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Interfaz gráfica principal del sistema TDA Pila de Libros.
 *
 * <p>Arquitectura: esta clase únicamente gestiona la vista y los eventos.
 * Toda la lógica LIFO reside en {@link back_end.Pila}; la GUI invoca sus
 * operaciones y refleja el estado resultante en el panel de visualización.</p>
 *
 * <p>Distribución de la ventana:</p>
 * <pre>
 *  ┌──────────────────────────────────────────────────────────┐
 *  │  PANEL FORMULARIO (izquierda)  │  PANEL PILA (derecha)  │
 *  │  · Campos: Título, Autor,      │  · JList que muestra   │
 *  │    Año, ISBN                   │    los libros apilados  │
 *  │  · Botones: Apilar, Desapilar, │    de tope a base       │
 *  │    Ver Tope, Vaciar, Tamaño    │  · Etiqueta de tope     │
 *  └──────────────────────────────────────────────────────────┘
 * </pre>
 */
public class Ventana extends JFrame {

    // -------------------------------------------------------------------------
    // Instancia del TDA Pila — única dependencia con el back-end
    // -------------------------------------------------------------------------
    private final Pila pila = new Pila();

    // -------------------------------------------------------------------------
    // Componentes del formulario de captura
    // -------------------------------------------------------------------------
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAnio;
    private JTextField txtIsbn;

    // -------------------------------------------------------------------------
    // Componentes de visualización
    // -------------------------------------------------------------------------
    private DefaultListModel<String> modeloPila;
    private JList<String>            listaPila;
    private JLabel                   lblTope;
    private JLabel                   lblTamanio;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public Ventana() {
        super("TDA Pila de Libros — LIFO");
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    // -------------------------------------------------------------------------
    // Construcción de la interfaz
    // -------------------------------------------------------------------------

    private void initComponents() {
        // Panel raíz con padding
        JPanel panelRaiz = new JPanel(new BorderLayout(10, 10));
        panelRaiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        panelRaiz.setBackground(new Color(245, 245, 250));

        panelRaiz.add(construirPanelFormulario(), BorderLayout.WEST);
        panelRaiz.add(construirPanelVisualizacion(), BorderLayout.CENTER);
        panelRaiz.add(construirPanelEstado(), BorderLayout.SOUTH);

        setContentPane(panelRaiz);
    }

    // ---- Panel izquierdo: formulario + botones --------------------------------

    private JPanel construirPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(310, 0));

        // Sub-panel de campos
        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        campos.setBorder(titledBorder("Datos del Libro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 6, 5, 6);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtTitulo = new JTextField(20);
        txtAutor  = new JTextField(20);
        txtAnio   = new JTextField(6);
        txtIsbn   = new JTextField(14);

        agregarCampo(campos, gbc, "Título:",  txtTitulo, 0);
        agregarCampo(campos, gbc, "Autor:",   txtAutor,  1);
        agregarCampo(campos, gbc, "Año:",     txtAnio,   2);
        agregarCampo(campos, gbc, "ISBN:",    txtIsbn,   3);

        // Sub-panel de botones
        JPanel botones = new JPanel(new GridLayout(5, 1, 6, 6));
        botones.setOpaque(false);
        botones.setBorder(titledBorder("Operaciones"));

        botones.add(crearBoton("▲  Apilar (Push)",       new Color(46, 125, 50),  this::accionApilar));
        botones.add(crearBoton("▼  Desapilar (Pop)",     new Color(198, 40, 40),  this::accionDesapilar));
        botones.add(crearBoton("🔍  Ver Tope (Peek)",    new Color(21, 101, 192), this::accionPeek));
        botones.add(crearBoton("🗑  Vaciar (Clear)",     new Color(130, 119, 23), this::accionVaciar));
        botones.add(crearBoton("📊  Tamaño (Size)",      new Color(74, 20, 140),  this::accionTamanio));

        panel.add(campos,  BorderLayout.NORTH);
        panel.add(botones, BorderLayout.CENTER);
        return panel;
    }

    // ---- Panel derecho: visualización de la pila -----------------------------

    private JPanel construirPanelVisualizacion() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(380, 480));
        panel.setBorder(titledBorder("Estado de la Pila"));

        // Etiqueta de tope
        lblTope = new JLabel("  Tope: (pila vacía)", JLabel.LEFT);
        lblTope.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTope.setForeground(new Color(46, 125, 50));
        lblTope.setBorder(new EmptyBorder(4, 4, 4, 4));

        // Lista que refleja los libros de tope a base
        modeloPila = new DefaultListModel<>();
        listaPila  = new JList<>(modeloPila);
        listaPila.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPila.setFont(new Font("Monospaced", Font.PLAIN, 12));
        listaPila.setCellRenderer(new RendererPila());
        listaPila.setFixedCellHeight(36);

        JScrollPane scroll = new JScrollPane(listaPila);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(189, 189, 189)));

        panel.add(lblTope, BorderLayout.NORTH);
        panel.add(scroll,  BorderLayout.CENTER);
        return panel;
    }

    // ---- Barra de estado inferior --------------------------------------------

    private JPanel construirPanelEstado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        lblTamanio = new JLabel("Elementos en la pila: 0");
        lblTamanio.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTamanio.setForeground(Color.DARK_GRAY);

        panel.add(new JLabel("ℹ"));
        panel.add(lblTamanio);
        return panel;
    }

    // -------------------------------------------------------------------------
    // Acciones de los botones (lógica de eventos — separada del TDA)
    // -------------------------------------------------------------------------

    /** Valida los campos y apila un nuevo Libro. */
    private void accionApilar(ActionEvent e) {
        String titulo = txtTitulo.getText().trim();
        String autor  = txtAutor.getText().trim();
        String isbnV  = txtIsbn.getText().trim();
        String anioTx = txtAnio.getText().trim();

        // Validación 1: campos vacíos
        if (titulo.isEmpty() || autor.isEmpty() || isbnV.isEmpty() || anioTx.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.\nPor favor, completa Título, Autor, Año e ISBN.");
            return;
        }

        // Validación 2: año numérico
        int anio;
        try {
            anio = Integer.parseInt(anioTx);
            if (anio <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            mostrarError("El campo Año debe contener un número entero positivo.\nEjemplo: 2023");
            txtAnio.requestFocus();
            txtAnio.selectAll();
            return;
        }

        Libro libro = new Libro(titulo, autor, anio, isbnV);
        pila.push(libro);
        limpiarFormulario();
        actualizarVisualizacion();
        mostrarInfo("Libro apilado correctamente:\n" + libro.toString());
    }

    /** Extrae el libro del tope y lo muestra. */
    private void accionDesapilar(ActionEvent e) {
        try {
            Libro extraido = pila.pop();
            actualizarVisualizacion();
            mostrarInfo("Libro desapilado (extraído del tope):\n" + extraido.toString());
        } catch (PilaVaciaException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /** Consulta el tope sin modificar la pila. */
    private void accionPeek(ActionEvent e) {
        try {
            Libro tope = pila.peek();
            mostrarInfo("Libro en el tope (sin extraer):\n" + tope.toString());
        } catch (PilaVaciaException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /** Vacía la pila completa. */
    private void accionVaciar(ActionEvent e) {
        if (pila.isEmpty()) {
            mostrarError("La pila ya está vacía.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas vaciar toda la pila? Esta acción eliminará los " + pila.size() + " elemento(s).",
                "Confirmar vaciado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            pila.clear();
            actualizarVisualizacion();
            mostrarInfo("La pila ha sido vaciada correctamente.");
        }
    }

    /** Muestra el número de elementos actuales. */
    private void accionTamanio(ActionEvent e) {
        int n = pila.size();
        String msg = n == 0
                ? "La pila está vacía (0 elementos)."
                : "La pila contiene " + n + " elemento" + (n == 1 ? "." : "s.");
        mostrarInfo(msg);
    }

    // -------------------------------------------------------------------------
    // Actualización de la visualización
    // -------------------------------------------------------------------------

    /**
     * Recorre la pila desde el tope usando una pila auxiliar temporal para
     * leer los datos sin destruir la estructura original, luego reconstruye
     * el DefaultListModel con el orden tope → base.
     */
    private void actualizarVisualizacion() {
        modeloPila.clear();

        // Usamos una pila auxiliar para recorrer sin alterar la original
        Pila auxiliar  = new Pila();
        Pila temporal  = new Pila();

        // Vaciar la pila original hacia la temporal (invierte el orden)
        while (!pila.isEmpty()) {
            temporal.push(pila.pop());
        }

        // Reconstruir la pila original y cargar la lista al mismo tiempo
        // El primero en salir de 'temporal' es la base; reapilamos en 'pila'
        // y construimos la vista de tope→base usando 'auxiliar'
        while (!temporal.isEmpty()) {
            Libro libro = temporal.pop();
            pila.push(libro);      // restaura la pila original
            auxiliar.push(libro);  // auxiliar queda en orden base→tope
        }

        // Ahora 'auxiliar' tiene base en el tope; lo vaciamos para construir
        // el modelo de lista en orden tope→base
        Pila paraVista = new Pila();
        while (!auxiliar.isEmpty()) {
            paraVista.push(auxiliar.pop()); // invierte → orden tope→base
        }

        int posicion = 1;
        while (!paraVista.isEmpty()) {
            Libro libro = paraVista.pop();
            String etiqueta = (posicion == 1 ? "▶ TOPE  " : "        ") + libro.toString();
            modeloPila.addElement(etiqueta);
            posicion++;
        }

        // Actualizar etiquetas de estado
        if (pila.isEmpty()) {
            lblTope.setText("  Tope: (pila vacía)");
            lblTope.setForeground(new Color(158, 158, 158));
        } else {
            lblTope.setText("  Tope: " + pila.peek().toString());
            lblTope.setForeground(new Color(46, 125, 50));
        }

        lblTamanio.setText("Elementos en la pila: " + pila.size());
    }

    // -------------------------------------------------------------------------
    // Utilidades de la vista
    // -------------------------------------------------------------------------

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtIsbn.setText("");
        txtTitulo.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    // -------------------------------------------------------------------------
    // Helpers de construcción de UI
    // -------------------------------------------------------------------------

    private void agregarCampo(JPanel panel, GridBagConstraints gbc,
                               String etiqueta, JTextField campo, int fila) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private JButton crearBoton(String texto, Color color,
                                java.util.function.Consumer<ActionEvent> accion) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(accion::accept);
        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.brighter());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private TitledBorder titledBorder(String titulo) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 189, 189)),
                titulo);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        border.setTitleColor(new Color(66, 66, 66));
        return border;
    }

    // -------------------------------------------------------------------------
    // Renderer personalizado para distinguir el tope visualmente
    // -------------------------------------------------------------------------

    private static class RendererPila extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            label.setBorder(new EmptyBorder(4, 8, 4, 8));
            if (index == 0) {
                // Elemento del tope: fondo verde claro
                if (!isSelected) {
                    label.setBackground(new Color(232, 245, 233));
                    label.setForeground(new Color(27, 94, 32));
                }
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else {
                if (!isSelected) {
                    label.setBackground(index % 2 == 0
                            ? new Color(250, 250, 250)
                            : Color.WHITE);
                    label.setForeground(Color.DARK_GRAY);
                }
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
            }
            return label;
        }
    }

    // -------------------------------------------------------------------------
    // Punto de entrada
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { /* usa el L&F por defecto */ }

        SwingUtilities.invokeLater(() -> new Ventana().setVisible(true));
    }
}
