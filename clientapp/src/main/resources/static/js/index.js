const main = document.getElementById('mainContent');
const navHome = document.getElementById('navHome');
const navAdmin = document.getElementById('navAdmin');
const navEvents = document.getElementById('navEvents');
const btnCreateSample = document.getElementById('btnCreateSample');

const KEY_EVENTS = 'eventify:events:v2';
const KEY_ORDERS = 'eventify:orders:v2';

let events = JSON.parse(localStorage.getItem(KEY_EVENTS)) || [];
let orders = JSON.parse(localStorage.getItem(KEY_ORDERS)) || [];

// ---------- Helpers ----------
const id = (prefix='id') => prefix + '_' + Math.random().toString(36).slice(2,9);
const rupiah = n => 'Rp ' + Number(n).toLocaleString('id-ID');
function save(){ localStorage.setItem(KEY_EVENTS, JSON.stringify(events)); localStorage.setItem(KEY_ORDERS, JSON.stringify(orders)); }

// ---------- Sample data (if empty) ----------
    function seed(){
    if(events.length) return;
    events = [
        { id:id('ev'), title:'Seminar UI/UX — Desain Masa Kini', date:'2025-12-10', price:250000, quota:120, sold:0, img:'https://images.unsplash.com/photo-1527251540020-9aa8f0f75b66?q=80&w=1200&auto=format&fit=crop', desc:'Pelajari prinsip UI/UX modern dan cara membangun portfolio profesional.' },
        { id:id('ev'), title:'Konser Indie Malam Merah', date:'2025-12-20', price:150000, quota:500, sold:0, img:'https://images.unsplash.com/photo-1508973375-0a7a9f60f685?q=80&w=1200&auto=format&fit=crop', desc:'Penampilan band indie terbaik dengan suasana intim dan enerjik.' },
        { id:id('ev'), title:'Workshop Python & Data Science', date:'2026-01-07', price:350000, quota:60, sold:0, img:'https://images.unsplash.com/photo-1555066931-4365d14bab8c?q=80&w=1200&auto=format&fit=crop', desc:'Dari dasar sintaks hingga praktik analisis data nyata.' }
    ];
    save();
    }

    // ---------- Render: Home (Hero + Grid) ----------
    function renderHome(){
    const hero = `
        <section class="card-hero mb-8 relative overflow-hidden">
        <div class="grid grid-cols-1 md:grid-cols-2 items-center">
            <div class="p-8 md:p-12">
            <h1 class="text-3xl md:text-4xl font-extrabold mb-3">Eventify — Temukan & Pesan Tiket dengan Mudah</h1>
            <p class="text-slate-600 mb-6">Solusi pembelian tiket online untuk seminar, konser, workshop. Cepat, aman, dan dapatkan e-ticket otomatis.</p>
            <div class="flex gap-3">
                <button id="btnExplore" class="px-5 py-3 rounded-lg bg-primary text-white shadow">Jelajahi Event</button>
                <button id="btnHow" class="px-5 py-3 rounded-lg border">Cara Kerja</button>
            </div>
            </div>
            <div class="p-4">
            <img src="https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=1400&auto=format&fit=crop" alt="hero" class="w-full h-64 md:h-full object-cover">
            </div>
        </div>
        </section>`;

    const gridStart = `<section><h2 class="text-2xl font-semibold mb-4">Event Populer</h2><div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">`;
    const gridEnd = `</div></section>`;
    let cards = '';
    events.forEach(ev=>{
        cards += `
        <article class="card p-0 overflow-hidden transform transition hover:-translate-y-2">
            <img class="event-img" src="${ev.img}" alt="${ev.title}" />
            <div class="p-4">
            <div class="flex items-start justify-between gap-3">
                <div>
                <h3 class="font-semibold text-lg">${ev.title}</h3>
                <p class="text-xs text-slate-500">${ev.date}</p>
                </div>
                <div class="text-right">
                <div class="price-badge">${rupiah(ev.price)}</div>
                </div>
            </div>
            <p class="text-slate-600 mt-3 text-sm">${ev.desc}</p>
            <div class="mt-4 flex items-center justify-between">
                <div class="text-xs text-slate-500">Kuota: ${ev.quota - (ev.sold||0)} tersisa</div>
                <div class="flex items-center gap-2">
                <button class="btn-secondary text-xs px-3 py-1" onclick="openDetail('${ev.id}')">Detail</button>
                <button class="px-3 py-2 rounded-md bg-accent text-white text-sm shadow" onclick="startBooking('${ev.id}')">Pesan Sekarang</button>
                </div>
            </div>
            </div>
        </article>`;
    });

    main.innerHTML = hero + gridStart + cards + gridEnd;
    // reveal animations
    document.querySelectorAll('.card').forEach((c,i)=>{ c.classList.add('fade-in'); setTimeout(()=> c.classList.add('show'), 80*i); });

    document.getElementById('btnExplore').addEventListener('click', ()=> window.scrollTo({top: document.querySelector('section + section').offsetTop - 20, behavior:'smooth'}));
    }

    // ---------- Event Detail & Booking ----------
    function openDetail(evId){
    const ev = events.find(x=>x.id===evId); if(!ev) return alert('Event tidak ditemukan');
    main.innerHTML = `
        <div class="grid md:grid-cols-3 gap-6">
        <div class="md:col-span-2">
            <div class="card p-0 overflow-hidden">
            <img src="${ev.img}" class="w-full h-72 object-cover" />
            <div class="p-6">
                <h2 class="text-2xl font-semibold">${ev.title}</h2>
                <p class="text-sm text-slate-500 mt-1">${ev.date} • ${ev.quota} kuota</p>
                <p class="mt-4 text-slate-700">${ev.desc}</p>
            </div>
            </div>
        </div>
        <aside class="card p-6">
            <div class="flex items-center justify-between">
            <div>
                <div class="text-xs text-slate-500">Harga</div>
                <div class="text-xl font-bold">${rupiah(ev.price)}</div>
            </div>
            <div class="text-xs text-slate-500">Tersisa: <span class="font-semibold">${ev.quota - (ev.sold||0)}</span></div>
            </div>
            <div class="mt-4">
            <label class="text-sm">Nama</label>
            <input id="bk_name" class="w-full border rounded-md p-2 mt-1" placeholder="Nama lengkap" />
            <label class="text-sm mt-3">Email</label>
            <input id="bk_email" class="w-full border rounded-md p-2 mt-1" placeholder="email@contoh.com" />
            <label class="text-sm mt-3">Jumlah tiket</label>
            <input id="bk_qty" type="number" min="1" max="${ev.quota - (ev.sold||0)}" value="1" class="w-full border rounded-md p-2 mt-1" />
            <button id="btnProceed" class="w-full mt-4 px-4 py-2 rounded-md bg-primary text-white">Lanjut ke Pembayaran</button>
            </div>
        </aside>
        </div>`;

    document.getElementById('btnProceed').addEventListener('click', ()=>{
        const name = document.getElementById('bk_name').value.trim();
        const email = document.getElementById('bk_email').value.trim();
        const qty = Number(document.getElementById('bk_qty').value);
        if(!name||!email) return alert('Masukkan nama dan email');
        if(qty < 1) return alert('Jumlah minimal 1');
        createOrder(ev.id, name, email, qty);
    });
    }

    function startBooking(evId){
    const ev = events.find(x=>x.id===evId); if(!ev) return;
    // quick booking modal-style inline
    main.innerHTML = `
        <div class="max-w-2xl mx-auto card p-6 text-center">
        <h2 class="text-xl font-semibold mb-2">Pesan: ${ev.title}</h2>
        <p class="text-slate-600 mb-4">Harga: <span class="font-bold">${rupiah(ev.price)}</span></p>
        <input id="bk_name" class="w-full border rounded-md p-2 mt-1 mb-2" placeholder="Nama" />
        <input id="bk_email" class="w-full border rounded-md p-2 mt-1 mb-2" placeholder="Email" />
        <input id="bk_qty" type="number" min="1" max="${ev.quota - (ev.sold||0)}" value="1" class="w-full border rounded-md p-2 mt-1 mb-4" />
        <div class="flex gap-3 justify-center">
            <button class="px-4 py-2 rounded-md bg-primary text-white" onclick="createOrder('${ev.id}', document.getElementById('bk_name').value, document.getElementById('bk_email').value, Number(document.getElementById('bk_qty').value))">Bayar & Pesan</button>
            <button class="px-4 py-2 rounded-md border" onclick="renderHome()">Batal</button>
        </div>
        </div>`;
    }

    // ---------- Orders & Payment (Simulated) ----------
    function createOrder(evId, name, email, qty){
    const ev = events.find(x=>x.id===evId); if(!ev) return alert('Event tidak ditemukan');
    if(ev.sold + qty > ev.quota) return alert('Kuota tidak mencukupi');
    if(!name||!email) return alert('Isi nama & email');

    const order = { id:id('ord'), evId, evTitle:ev.title, name, email, qty, total: ev.price * qty, status:'PENDING', createdAt: new Date().toLocaleString() };
    orders.push(order); save();
    openPaymentPage(order.id);
    }

    function openPaymentPage(orderId){
    const order = orders.find(o=>o.id===orderId); if(!order) return;
    main.innerHTML = `
        <div class="max-w-md mx-auto card p-6 text-center">
        <h3 class="text-lg font-semibold">Pembayaran — QRIS</h3>
        <p class="text-slate-600">Total: <span class="font-bold">${rupiah(order.total)}</span></p>
        <div id="qrcode" class="mt-4 flex justify-center"></div>
        <div class="mt-4 flex gap-3 justify-center">
            <button id="btnSimPay" class="px-4 py-2 rounded-md bg-accent text-white">Simulasi Bayar</button>
            <button id="btnCancel" class="px-4 py-2 rounded-md border">Batal</button>
        </div>
        </div>`;
    // render QR
    const qrtarget = document.getElementById('qrcode'); qrtarget.innerHTML = '';
    new QRCode(qrtarget, { text: JSON.stringify({orderId: order.id, amount: order.total}), width:160, height:160 });
    document.getElementById('btnSimPay').addEventListener('click', ()=> simulateGateway(order.id));
    document.getElementById('btnCancel').addEventListener('click', ()=> renderHome());
    }

    function simulateGateway(orderId){
    // simulate processing
    main.innerHTML = `<div class="max-w-md mx-auto card p-6 text-center"><p class="text-slate-600">Memproses pembayaran...</p></div>`;
    setTimeout(()=>{
        const ord = orders.find(o=>o.id===orderId); if(!ord) return alert('Order tidak ditemukan');
        ord.status = 'PAID'; ord.paidAt = new Date().toLocaleString();
        // reduce quota
        const ev = events.find(e=>e.id===ord.evId); if(ev){ ev.sold = (ev.sold||0) + ord.qty; }
        save();
        // confetti
        confetti({ particleCount: 80, spread: 70, origin: { y: 0.4 } });
        // generate ticket
        generatePDFTicket(ord);
        // show success
        main.innerHTML = `<div class="max-w-md mx-auto card p-6 text-center"><h3 class="text-lg font-semibold text-green-600">Pembayaran Berhasil!</h3><p class="mt-2">E-ticket telah dibuat dan (simulasi) dikirim ke <b>${ord.email}</b>.</p><div class="mt-4 flex gap-3 justify-center"><button class="px-4 py-2 rounded-md bg-primary text-white" onclick="renderHome()">Kembali ke Home</button></div></div>`;
    }, 1200);
    }

    function generatePDFTicket(order){
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF({ unit:'pt', format:'a4' });
    doc.setFontSize(18); doc.text('E-Ticket — Eventify', 40, 60);
    doc.setFontSize(12); doc.text(`Event: ${order.evTitle}`, 40, 100);
    doc.text(`Nama : ${order.name}`, 40, 120);
    doc.text(`Email : ${order.email}`, 40, 140);
    doc.text(`Jumlah : ${order.qty}`, 40, 160);
    doc.text(`Total : ${rupiah(order.total)}`, 40, 180);
    doc.text(`Order ID : ${order.id}`, 40, 200);
    doc.save(`eticket_${order.id}.pdf`);
    }

    // ---------- Admin (sidebar + dashboard) ----------
    function renderAdminLogin(){
    main.innerHTML = `
        <div class="max-w-md mx-auto card p-6 text-center">
        <h2 class="text-xl font-semibold mb-4">Login Admin</h2>
        <input id="adminPass" type="password" class="w-full border rounded-md p-2 mb-4" placeholder="Password admin" />
        <button class="w-full px-4 py-2 rounded-md bg-primary text-white" onclick="checkAdmin()">Login</button>
        </div>`;
    }

    function checkAdmin(){
    const pass = document.getElementById('adminPass').value;
    if(pass === 'admin123') renderAdminPanel();
    else alert('Password salah');
    }

    function renderAdminPanel(){
    // prepare stats
    const totalEvents = events.length;
    const totalTrans = orders.length;
    const ticketsSold = orders.filter(o=>o.status==='PAID').reduce((s,o)=>s+o.qty,0);
    const revenue = orders.filter(o=>o.status==='PAID').reduce((s,o)=>s+o.total,0);

    main.innerHTML = `
        <div class="flex gap-6">
        <aside class="sidebar card p-4">
            <h3 class="font-semibold mb-4">Admin</h3>
            <div class="space-y-2 text-sm">
            <button class="w-full text-left px-3 py-2 rounded hover:bg-slate-100" onclick="renderAdminPanel()">Dashboard</button>
            <button class="w-full text-left px-3 py-2 rounded hover:bg-slate-100" onclick="renderManageEvents()">Kelola Event</button>
            <button class="w-full text-left px-3 py-2 rounded hover:bg-slate-100" onclick="renderTransactions()">Transaksi</button>
            <button class="w-full text-left px-3 py-2 rounded hover:bg-slate-100 text-red-600" onclick="logoutAdmin()">Logout</button>
            </div>
        </aside>
        <section class="flex-1">
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
            <div class="card p-4"><div class="text-sm text-slate-500">Total Event</div><div class="text-xl font-bold">${totalEvents}</div></div>
            <div class="card p-4"><div class="text-sm text-slate-500">Transaksi</div><div class="text-xl font-bold">${totalTrans}</div></div>
            <div class="card p-4"><div class="text-sm text-slate-500">Tiket Terjual</div><div class="text-xl font-bold">${ticketsSold}</div></div>
            </div>
            <div class="card p-4 mb-6">
            <h4 class="font-semibold mb-2">Pendapatan Bulanan (Simulasi)</h4>
            <canvas id="chartRevenue" height="120"></canvas>
            </div>
            <div id="adminFooter" class="text-sm text-slate-600">Ringkasan: Pendapatan total <b>${rupiah(revenue)}</b></div>
        </section>
        </div>`;

    // draw chart
    setTimeout(()=>{
        const ctx = document.getElementById('chartRevenue').getContext('2d');
        // dummy monthly data derived from paid orders
        const labels = ['Jan','Feb','Mar','Apr','Mei','Jun','Jul','Agt','Sep','Okt','Nov','Des'];
        const data = labels.map((l,i)=> Math.floor(Math.random()*200000 + (i*10000)) );
        new Chart(ctx, { type:'line', data:{ labels, datasets:[{ label:'Pendapatan (Rp)', data, fill:true, tension:0.3, backgroundColor:'rgba(99,102,241,0.12)', borderColor:'#6366f1' }]}, options:{ responsive:true, maintainAspectRatio:false } });
    },200);
    }

    function renderManageEvents(){
    let html = `<div class="card p-4 mb-4"><h3 class="font-semibold mb-2">Tambah Event</h3>
        <div class="grid grid-cols-1 gap-2">
        <input id="ev_title" class="border rounded p-2" placeholder="Judul event" />
        <input id="ev_date" class="border rounded p-2" type="date" />
        <input id="ev_price" class="border rounded p-2" type="number" placeholder="Harga (Rp)" />
        <input id="ev_quota" class="border rounded p-2" type="number" placeholder="Kuota" />
        <input id="ev_img" class="border rounded p-2" placeholder="URL gambar (opsional)" />
        <textarea id="ev_desc" class="border rounded p-2" placeholder="Deskripsi singkat"></textarea>
        <div class="flex gap-2"><button class="btn-primary" onclick="saveEvent()">Simpan</button><button class="border px-3 py-2 rounded" onclick="renderAdminPanel()">Batal</button></div>
        </div></div>`;

    html += `<div class="space-y-2">`;
    events.forEach((ev,i)=>{
        html += `<div class="card p-3 flex justify-between items-center"><div><div class="font-semibold">${ev.title}</div><div class="text-xs text-slate-500">${ev.date} • ${rupiah(ev.price)}</div></div><div class="flex gap-2"><button class="border px-2 py-1 rounded" onclick="editEvent('${ev.id}')">Edit</button><button class="border px-2 py-1 rounded text-red-600" onclick="deleteEvent('${ev.id}')">Hapus</button></div></div>`;
    });
    html += `</div>`;
    document.querySelector('section.flex-1').innerHTML = html;
    }

    function saveEvent(){
    const title = document.getElementById('ev_title').value.trim();
    const date = document.getElementById('ev_date').value;
    const price = Number(document.getElementById('ev_price').value);
    const quota = Number(document.getElementById('ev_quota').value);
    const img = document.getElementById('ev_img').value || 'https://images.unsplash.com/photo-1508973375-0a7a9f60f685?q=80&w=1200&auto=format&fit=crop';
    const desc = document.getElementById('ev_desc').value.trim();
    if(!title||!date||!price||!quota) return alert('Lengkapi data event');
    events.push({ id:id('ev'), title, date, price, quota, sold:0, img, desc }); save();
    alert('Event tersimpan'); renderAdminPanel();
    }

    function editEvent(evId){
    const ev = events.find(e=>e.id===evId); if(!ev) return;
    renderManageEvents();
    setTimeout(()=>{
        document.getElementById('ev_title').value = ev.title;
        document.getElementById('ev_date').value = ev.date;
        document.getElementById('ev_price').value = ev.price;
        document.getElementById('ev_quota').value = ev.quota;
        document.getElementById('ev_img').value = ev.img;
        document.getElementById('ev_desc').value = ev.desc;
        // replace saveEvent to update
        window.saveEvent = function(){ ev.title = document.getElementById('ev_title').value; ev.date = document.getElementById('ev_date').value; ev.price = Number(document.getElementById('ev_price').value); ev.quota = Number(document.getElementById('ev_quota').value); ev.img = document.getElementById('ev_img').value; ev.desc = document.getElementById('ev_desc').value; save(); alert('Event diperbarui'); renderAdminPanel(); };
    },100);
    }

    function deleteEvent(evId){ if(!confirm('Hapus event ini?')) return; events = events.filter(e=>e.id!==evId); save(); renderAdminPanel(); }

    function renderTransactions(){
    let html = `<div class="card p-4"><h3 class="font-semibold mb-2">Daftar Transaksi</h3>`;
    orders.slice().reverse().forEach(o=>{
        html += `<div class="p-3 border-b"><div class="flex justify-between"><div><b>${o.evTitle}</b><div class="text-xs text-slate-500">${o.name} • ${o.email}</div></div><div class="text-right"><div>${rupiah(o.total)}</div><div class="text-xs ${o.status==='PAID'?'text-green-600':'text-yellow-600'}">${o.status}</div></div></div></div>`;
    });
    html += `</div>`;
    document.querySelector('section.flex-1').innerHTML = html;
    }

function logoutAdmin(){ renderHome(); }

// ---------- Init & Events ----------
seed(); renderHome();

navHome.addEventListener('click', (e)=>{ e.preventDefault(); renderHome(); });
navEvents.addEventListener('click', (e)=>{ e.preventDefault(); renderHome(); });
navAdmin.addEventListener('click', (e)=>{ e.preventDefault(); renderAdminLogin(); });
btnCreateSample.addEventListener('click', (e)=>{ renderAdminLogin(); });

// expose for debugging
window._Eventify = { events, orders, save };