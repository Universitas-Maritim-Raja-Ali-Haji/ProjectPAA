/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HP
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeekAndHideGame extends JFrame {
    private static final int UKURAN_PETA = 12;
    private static final int UKURAN_SELA = 50;
    private int jumlahDroidMerah = 0;
    private JButton tombolMulai;
    private JButton tombolJeda;
    private JButton tombolAcakPosisi;
    private JButton tombolTambahDroidMerah;
    private JButton tombolAcakPeta;

    private JSlider sliderJarakPandangDroidHijau;

    private JPanel panelPeta;
    private Sel[][] sel;

    private Sel selDroidMerah;
    private Sel selDroidHijau;

    private boolean permainanBerjalan;

    public SeekAndHideGame() {
        setTitle("Permainan Seek and Hide");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        inisialisasiAntarmuka();

        pack();
        setLocationRelativeTo(null);
    }

    private void inisialisasiAntarmuka() {
        tombolMulai = new JButton("Mulai");
        tombolJeda = new JButton("Jeda");
        tombolAcakPosisi = new JButton("Acak Posisi");
        tombolTambahDroidMerah = new JButton("Tambah Droid Merah");
        tombolAcakPeta = new JButton("Acak Peta");

        sliderJarakPandangDroidHijau = new JSlider(1, 5, 3);
        sliderJarakPandangDroidHijau.setMajorTickSpacing(1);
        sliderJarakPandangDroidHijau.setPaintTicks(true);
        sliderJarakPandangDroidHijau.setSnapToTicks(true);
        sliderJarakPandangDroidHijau.setPaintLabels(true);

        panelPeta = new JPanel();
        panelPeta.setLayout(new GridLayout(UKURAN_PETA, UKURAN_PETA));
        panelPeta.setPreferredSize(new Dimension(UKURAN_PETA * UKURAN_SELA, UKURAN_PETA * UKURAN_SELA));

        inisialisasiSel();

        JPanel panelKontrol = new JPanel();
        panelKontrol.add(tombolMulai);
        panelKontrol.add(tombolJeda);
        panelKontrol.add(tombolAcakPosisi);
        panelKontrol.add(tombolTambahDroidMerah);
        panelKontrol.add(new JLabel("Jarak Pandang Droid Hijau:"));
        panelKontrol.add(sliderJarakPandangDroidHijau);
        panelKontrol.add(tombolAcakPeta);

        setLayout(new BorderLayout());
        add(panelPeta, BorderLayout.CENTER);
        add(panelKontrol, BorderLayout.SOUTH);

        tombolMulai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mulaiPermainan();
            }
        });

        tombolJeda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jedaPermainan();
            }
        });

        tombolAcakPosisi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acakPosisi();
            }
        });

        tombolTambahDroidMerah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahDroidMerah();
            }
        });

        tombolAcakPeta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acakPeta();
            }
        });
    }

    private void inisialisasiSel() {
        sel = new Sel[UKURAN_PETA][UKURAN_PETA];

        for (int baris = 0; baris < UKURAN_PETA; baris++) {
            for (int kolom = 0; kolom < UKURAN_PETA; kolom++) {
                Sel sel = new Sel(baris, kolom);
                this.sel[baris][kolom] = sel;
                panelPeta.add(sel);
            }
        }
    }

    private void mulaiPermainan() {
    permainanBerjalan = true;

    // Dapatkan jarak pandang droid hijau dari slider
    int jarakPandang = sliderJarakPandangDroidHijau.getValue();

    // Buat timer untuk mengatur pergerakan droid hijau dan droid merah
    Timer timer = new Timer(250, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!permainanBerjalan) {
                // Jika permainan dijeda, hentikan pergerakan droid hijau dan droid merah
                ((Timer) e.getSource()).stop();
                return;
            }

            // Pergerakan otomatis droid hijau
            int barisHijau = selDroidHijau.getBaris();
            int kolomHijau = selDroidHijau.getKolom();

            // Pergerakan otomatis droid merah
            int barisMerah = selDroidMerah.getBaris();
            int kolomMerah = selDroidMerah.getKolom();

            // Hitung jarak antara droid merah dan droid hijau
            int jarak = Math.abs(barisMerah - barisHijau) + Math.abs(kolomMerah - kolomHijau);

            if (jarak <= jarakPandang) {
                // Jika droid hijau berada dalam jarak pandang, droid merah mengejar droid hijau

                // Cek posisi relatif droid merah dan droid hijau
                int relatifBaris = barisMerah - barisHijau;
                int relatifKolom = kolomMerah - kolomHijau;

                // Pergerakan droid merah
                int nextBarisMerah = barisMerah;
                int nextKolomMerah = kolomMerah;

                // Jika droid merah berada di atas/bawah droid hijau, gerakkan droid merah ke atas/bawah
                if (Math.abs(relatifBaris) >= Math.abs(relatifKolom)) {
                    if (relatifBaris > 0) {
                        nextBarisMerah--;
                    } else {
                        nextBarisMerah++;
                    }
                }
                // Jika droid merah berada di kiri/kanan droid hijau, gerakkan droid merah ke kiri/kanan
                else {
                    if (relatifKolom > 0) {
                        nextKolomMerah--;
                    } else {
                        nextKolomMerah++;
                    }
                }

                // Periksa apakah langkah berikutnya valid (tidak melewati tembok dan tidak keluar dari peta)
                if (nextBarisMerah >= 0 && nextBarisMerah < UKURAN_PETA && nextKolomMerah >= 0 && nextKolomMerah < UKURAN_PETA
                        && sel[nextBarisMerah][nextKolomMerah].getBackground() != Color.BLACK) {
                    selDroidMerah.setWarna(null);
                    selDroidMerah = sel[nextBarisMerah][nextKolomMerah];
                    selDroidMerah.setWarna(Color.RED);

                    // Cek apakah droid merah menangkap droid hijau
                    if (nextBarisMerah == barisHijau && nextKolomMerah == kolomHijau) {
                        permainanBerjalan = false;
                        JOptionPane.showMessageDialog(null, "Droid Merah berhasil menangkap Droid Hijau!");
                    }
                }
            } else {
                // Jika droid hijau berada di luar jarak pandang, droid hijau bergerak secara acak

                // Generate langkah acak untuk droid hijau
                int[] langkahBaris = {-1, 0, 0, 1};
                int[] langkahKolom = {0, -1, 1, 0};
                int randomIndex = (int) (Math.random() * langkahBaris.length);
                int langkahBarisHijau = langkahBaris[randomIndex];
                int langkahKolomHijau = langkahKolom[randomIndex];

                // Pergerakan droid hijau
                int nextBarisHijau = barisHijau + langkahBarisHijau;
                int nextKolomHijau = kolomHijau + langkahKolomHijau;

                // Periksa apakah langkah berikutnya valid (tidak melewati tembok dan tidak keluar dari peta)
                if (nextBarisHijau >= 0 && nextBarisHijau < UKURAN_PETA && nextKolomHijau >= 0 && nextKolomHijau < UKURAN_PETA
                        && sel[nextBarisHijau][nextKolomHijau].getBackground() != Color.BLACK) {
                    selDroidHijau.setWarna(null);
                    selDroidHijau = sel[nextBarisHijau][nextKolomHijau];
                    selDroidHijau.setWarna(Color.GREEN);
                }
            }
        }
    });

    // Mulai timer
    timer.start();
}


    private void jedaPermainan() {
        permainanBerjalan = false;
        
    // Mengaktifkan kembali tombol-tombol kontrol setelah permainan dijeda
    tombolMulai.setEnabled(true);
    tombolAcakPosisi.setEnabled(true);
    tombolTambahDroidMerah.setEnabled(true);
    tombolAcakPeta.setEnabled(true);
    }

    private void acakPosisi() {
        if (permainanBerjalan) {
            JOptionPane.showMessageDialog(this, "Jeda permainan sebelum mengacak posisi.", "Jeda Permainan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        hapusDroid();

        // Acak posisi droid merah
        int barisMerah = (int) (Math.random() * UKURAN_PETA);
        int kolomMerah = (int) (Math.random() * UKURAN_PETA);
        selDroidMerah = sel[barisMerah][kolomMerah];
        selDroidMerah.setWarna(Color.RED);

        // Acak posisi droid hijau
        int barisHijau = (int) (Math.random() * UKURAN_PETA);
        int kolomHijau = (int) (Math.random() * UKURAN_PETA);
        selDroidHijau = sel[barisHijau][kolomHijau];
        selDroidHijau.setWarna(Color.GREEN);
    }

    private void tambahDroidMerah() {
        if (permainanBerjalan) {
        JOptionPane.showMessageDialog(this, "Jeda permainan sebelum menambahkan droid merah.", "Jeda Permainan", JOptionPane.WARNING_MESSAGE);
        return;
        }

        if (jumlahDroidMerah >= 3) {
        JOptionPane.showMessageDialog(this, "Jumlah droid merah sudah mencapai batas (3).", "Batas Droid Merah", JOptionPane.WARNING_MESSAGE);
        return;
        }

        // Acak posisi droid merah
        int barisMerah = (int) (Math.random() * UKURAN_PETA);
        int kolomMerah = (int) (Math.random() * UKURAN_PETA);
        selDroidMerah = sel[barisMerah][kolomMerah];
        selDroidMerah.setWarna(Color.RED);
        jumlahDroidMerah++;
        }

    private void hapusDroid() {
        if (selDroidMerah != null) {
            selDroidMerah.setWarna(null);
            selDroidMerah = null;
        }

        if (selDroidHijau != null) {
            selDroidHijau.setWarna(null);
            selDroidHijau = null;
        }
    }

    private void acakPeta() {
        if (permainanBerjalan) {
            JOptionPane.showMessageDialog(this, "Jeda permainan sebelum mengacak peta.", "Jeda Permainan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        hapusDroid();

        int[][] peta = generatePeta(UKURAN_PETA, UKURAN_PETA);

        for (int baris = 0; baris < UKURAN_PETA; baris++) {
            for (int kolom = 0; kolom < UKURAN_PETA; kolom++) {
                Sel sel = this.sel[baris][kolom];
                if (peta[baris][kolom] == 1) {
                    sel.setBackground(Color.BLACK);
                } else {
                    sel.setBackground(Color.WHITE);
                }
            }
        }
    }

    private int[][] generatePeta(int baris, int kolom) {
        int[][] peta = new int[baris][kolom];

        // Setiap sel dengan nilai 1 akan dianggap sebagai tembok
        // Sedangkan sel dengan nilai 0 akan dianggap sebagai jalan
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                peta[i][j] = Math.random() < 0.3 ? 1 : 0;
            }
        }

        return peta;
    }

    private class Sel extends JPanel {
        private int baris;
        private int kolom;
        private Color warna;

        public Sel(int baris, int kolom) {
            this.baris = baris;
            this.kolom = kolom;
            this.warna = null;

            setPreferredSize(new Dimension(UKURAN_SELA, UKURAN_SELA));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        public int getBaris() {
            return baris;
        }

        public int getKolom() {
            return kolom;
        }

        public void setWarna(Color warna) {
            this.warna = warna;
            setBackground(warna);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SeekAndHideGame().setVisible(true);
            }
        });
    }
}