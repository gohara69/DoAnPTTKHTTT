/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

import DAO.NguyenLieuDAO;
import DAO.NhaCungCapDAO;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.ComboBoxItem;
import model.NguyenLieu;
import model.NhaCungCap;
import swing.combobox;
import swing.scrollbar;

/**
 *
 * @author VU HOANG
 */
public class frmNhapNguyenLieu extends javax.swing.JFrame {
    Vector data = new Vector();
    TableModelListener modelTableListener = new TableModelListener(){
        @Override
        public void tableChanged(TableModelEvent e) {
            int row = tblNguyenLieu.getSelectedRow();
            int col = tblNguyenLieu.getSelectedColumn();
            if(col == 0){
                handleWhenEditColId(row, col);
            } else if (col == 1) {
                handleWhenEditColTenNguyenLieu(row, col);
            } else if (col == 3) {
                handleWhenEditColSoLuong(row, col);
            }
        }
    };
    /**
     * Creates new form frmNhapNguyenLieu
     */
    public frmNhapNguyenLieu() {
        initComponents();
        spTable.setVerticalScrollBar(new scrollbar());
        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        spTable.setVerticalScrollBar(new scrollbar());
        
        txtHoaDonID.setHint("");
        txtTenNhanVien.setHint("");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
        LocalDate now = LocalDate.now();  
        txtNgayNhap.setHint(now + "");
        txtThanhTien.setHint("");
        tblNguyenLieu.getModel().addTableModelListener(modelTableListener);
        txtTenNhanVien.setText("Nguyễn Hữu Hòa");
        
        cboNhaCungCap.removeAllItems();
        ArrayList<NhaCungCap> dsNCC = NhaCungCapDAO.layDanhSachNhaCungCap();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(NhaCungCap a : dsNCC){
            model.addElement(new ComboBoxItem(a.getNCC_ID(), a.getNCC_TEN()));
        }
        cboNhaCungCap.setModel(model);
        getDataToColNL_IDFromNCC_ID();
        getDataToColTenNguyenLieuFromNCC_ID();
    }
    
    public void updateTable(){
        tblNguyenLieu.getModel().removeTableModelListener(modelTableListener);
        clear();
        for(int i = 0 ; i < data.size() ; i++){
            tblNguyenLieu.addRow((Vector) data.get(i));
        }
        tblNguyenLieu.getModel().addTableModelListener(modelTableListener);
    }
    
    public void updateTableWhenEnterQuantity(){
        tblNguyenLieu.getModel().removeTableModelListener(modelTableListener);
        clear();
        for(int i = 0 ; i < data.size() ; i++){
            tblNguyenLieu.addRow((Vector) data.get(i));
        }
        Vector nul = new Vector();
        nul.add("");
        nul.add("");
        nul.add("");
        nul.add("");
        nul.add("");
        tblNguyenLieu.addRow(nul);
        tblNguyenLieu.getModel().addTableModelListener(modelTableListener);
    }
    
    public void clear(){
        DefaultTableModel dtm = (DefaultTableModel) tblNguyenLieu.getModel();
        dtm.setRowCount(0);
    }
    
    public void getDataToColNL_IDFromNCC_ID(){
        int nccId = ((ComboBoxItem)cboNhaCungCap.getSelectedItem()).getKey();
        NhaCungCap ncc = new NhaCungCap();
        ncc.setNCC_ID(nccId);
        ArrayList<Integer> lstIngredientId = NguyenLieuDAO.searchIngredientByNCCId(ncc);
        combobox comboId = new combobox();
        for(int i = 0 ; i < lstIngredientId.size() ; i++){
            comboId.addItem(lstIngredientId.get(i) + "");
        }
        TableColumn col = tblNguyenLieu.getColumnModel().getColumn(0);
        col.setCellEditor(new DefaultCellEditor(comboId));
    }
    
    public void getDataToColTenNguyenLieuFromNCC_ID(){
        int nccId = ((ComboBoxItem)cboNhaCungCap.getSelectedItem()).getKey();
        NhaCungCap ncc = new NhaCungCap();
        ncc.setNCC_ID(nccId);
        ArrayList<NguyenLieu> lstIngredientId = NguyenLieuDAO.searchIngredientNameByNCCId(ncc);
        combobox comboId = new combobox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(int i = 0 ; i < lstIngredientId.size() ; i++){
            model.addElement(new ComboBoxItem(lstIngredientId.get(i).getNL_ID(), lstIngredientId.get(i).getNL_TEN()));
        }
        comboId.setModel(model);
        TableColumn col = tblNguyenLieu.getColumnModel().getColumn(1);
        col.setCellEditor(new DefaultCellEditor(comboId));
    }
    
    public void handleWhenEditColId(int row, int col){
        String id = (String) tblNguyenLieu.getValueAt(row, col);
        try{
            int IngredientId = Integer.parseInt(id);
            NguyenLieu nl = new NguyenLieu();
            nl.setNL_ID(IngredientId);
            
            if(data.isEmpty() || "".equals((String) tblNguyenLieu.getValueAt(row, 4))){
                handleWhenEditColIdNotEditQuantity(nl);
            } else {
                handleWhenEditColIdEditQuantity(nl, row);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(frmNhapNguyenLieu.this, "Vui lòng nhập mã nguyên liệu dòng 142", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void handleWhenEditColIdNotEditQuantity(NguyenLieu nl){
        ArrayList<NguyenLieu> dsNL = NguyenLieuDAO.searchIngredientById(nl);
        for(NguyenLieu nlieu: dsNL){
            Vector info = new Vector();
            info.add(nlieu.getNL_ID());
            info.add(nlieu.getNL_TEN());
            info.add(nlieu.getNL_DONVITINH());
            info.add("");
            info.add("");
                        
            data.add(info);
            updateTable();
        }
    }
    
    public void handleWhenEditColIdEditQuantity(NguyenLieu nl, int row){
        ArrayList<NguyenLieu> dsNL = NguyenLieuDAO.searchIngredientById(nl);
        Vector info = (Vector) data.get(row);
        Float gia = NguyenLieuDAO.searchIngredientPriceById(nl);
        for(NguyenLieu nlieu: dsNL){
            info.set(0, nlieu.getNL_ID());
            info.set(1, nlieu.getNL_TEN());
            info.set(2, nlieu.getNL_DONVITINH());
            info.set(4,"" + gia * Integer.parseInt((String) info.get(3)));
        }
        updateTable();
        sumTotalPrice();
    }
    
    public void handleWhenEditColTenNguyenLieu(int row, int col){
        int id = ((ComboBoxItem)tblNguyenLieu.getValueAt(row, col)).getKey();
        try{
            NguyenLieu nl = new NguyenLieu();
            nl.setNL_ID(id);
            
            if(data.isEmpty() || "".equals((String) tblNguyenLieu.getValueAt(row, 4))){
                handleWhenEditColIdNotEditQuantity(nl); 
            } else {
                handleWhenEditColIdEditQuantity(nl, row);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(frmNhapNguyenLieu.this, "Vui lòng nhập mã nguyên liệu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void handleWhenEditColSoLuong(int row, int col){
        if(tblNguyenLieu.getValueAt(row, 0) == null || tblNguyenLieu.getValueAt(row, 1) == null ) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu để nhập trước khi chọn số lượng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            updateTable();
            return;
        } 
        
        int id = (Integer)tblNguyenLieu.getValueAt(row, 0);
        Vector info = (Vector) data.get(row);
        if(Integer.parseInt((String) info.get(3)) <= 0){
            JOptionPane.showMessageDialog(this, "Số lượng cần nhập phải lớn hơn 0", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            updateTable();
            return;
        }
        try{
            NguyenLieu nl = new NguyenLieu();
            nl.setNL_ID(id);
            Float gia = NguyenLieuDAO.searchIngredientPriceById(nl);
            boolean flag = ("".equals(((Vector) data.get(row)).get(4)));
            info.set(4,"" + gia * Integer.parseInt((String) info.get(3)));
            updateTableWhenEnterQuantity();
            sumTotalPrice();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(frmNhapNguyenLieu.this, "Vui lòng nhập số lượng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void sumTotalPrice(){
        float sum = 0;
        for(int i = 0 ; i < data.size() ; i++){
            sum+= Float.parseFloat((String)(((Vector)data.get(i)).get(4)));
        }
        txtThanhTien.setText(sum + "");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new swing.panelBorder();
        btnBack = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtHoaDonID = new swing.searchText();
        jLabel2 = new javax.swing.JLabel();
        txtThanhTien = new swing.searchText();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTenNhanVien = new swing.searchText();
        panelBorder2 = new swing.panelBorder();
        spTable = new javax.swing.JScrollPane();
        tblNguyenLieu = new swing.table();
        button1 = new swing.button();
        jLabel5 = new javax.swing.JLabel();
        txtNgayNhap = new swing.searchText();
        cboNhaCungCap = new swing.combobox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/arrow.png"))); // NOI18N
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Mã hóa đơn");

        txtHoaDonID.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Nhân viên");

        txtThanhTien.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Nhà cung cấp");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Ngày nhập");

        txtTenNhanVien.setEnabled(false);

        panelBorder2.setBackground(new java.awt.Color(255, 255, 255));

        spTable.setBorder(null);

        tblNguyenLieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NL_ID", "Tên nguyên liệu", "Đơn vị tính", "Số lượng", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spTable.setViewportView(tblNguyenLieu);

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spTable, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        button1.setText("Nhập nguyên liệu");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Thành tiền");

        txtNgayNhap.setEnabled(false);

        cboNhaCungCap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hải Chiều", "Gia Long", "Tự Đức" }));
        cboNhaCungCap.setLabeText("");
        cboNhaCungCap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboNhaCungCapMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnBack)
                        .addGroup(panelBorder1Layout.createSequentialGroup()
                            .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2))
                            .addGap(14, 14, 14)
                            .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelBorder1Layout.createSequentialGroup()
                                    .addComponent(txtTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(141, 141, 141))
                                .addGroup(panelBorder1Layout.createSequentialGroup()
                                    .addComponent(txtHoaDonID, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(29, 29, 29)))
                            .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNgayNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboNhaCungCap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(panelBorder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnBack)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1))
                    .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHoaDonID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(cboNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(txtTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBorder2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        getContentPane().add(panelBorder1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnBackMouseClicked

    private void cboNhaCungCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboNhaCungCapMouseClicked
        getDataToColNL_IDFromNCC_ID();
        getDataToColTenNguyenLieuFromNCC_ID();
    }//GEN-LAST:event_cboNhaCungCapMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmNhapNguyenLieu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmNhapNguyenLieu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmNhapNguyenLieu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmNhapNguyenLieu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmNhapNguyenLieu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnBack;
    private swing.button button1;
    private swing.combobox cboNhaCungCap;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private swing.panelBorder panelBorder1;
    private swing.panelBorder panelBorder2;
    private javax.swing.JScrollPane spTable;
    private swing.table tblNguyenLieu;
    private swing.searchText txtHoaDonID;
    private swing.searchText txtNgayNhap;
    private swing.searchText txtTenNhanVien;
    private swing.searchText txtThanhTien;
    // End of variables declaration//GEN-END:variables

    private boolean typeof(int IngredientId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
