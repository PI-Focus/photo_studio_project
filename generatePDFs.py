import os
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas

target_dir = "src/main/resources/static/docs"
os.makedirs(target_dir, exist_ok=True)

docs = {
    "privacy.pdf": "PRIVACY POLICY",
    "agreement.pdf": "USER AGREEMENT",
    "offer.pdf": "PUBLIC OFFER"
}

for filename, title in docs.items():
    filepath = os.path.join(target_dir, filename)
    c = canvas.Canvas(filepath, pagesize=letter)
    
    c.setFont("Helvetica-Bold", 22)
    c.drawString(50, 700, title)
    
    c.setFont("Helvetica", 12)
    c.drawString(50, 650, "Status: DEMO / TEMPLATE")
    c.drawString(50, 620, "This document was generated automatically for demonstration purposes only.")
    c.drawString(50, 600, "It contains no real legal obligations and serves as a placeholder for the design layout.")
    c.drawString(50, 560, "All rights reserved. PI-FOCUS (c) 2026.")
    
    c.save()

print("PDF-документы успешно сгенерированы в папку static/docs!")
