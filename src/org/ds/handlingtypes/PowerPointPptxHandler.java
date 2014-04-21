package org.ds.handlingtypes;

import java.io.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.ds.handlingtypes.filehandler.*;

/**
 * ����Microsoft PowerPoint 2007/2010 �ļ�
 */
public class PowerPointPptxHandler implements FileHandler
{

    /**
     * ��pptx�ļ���ȡһ��Document���ʵ��
     * @param file �ļ�����
     * @return һ���µ�Document���ʵ��
     * @throws FileHandlerException DocumentHandler�쳣
     */
    @Override
    public Document getDocument(File file)
            throws FileHandlerException
    {
        String bodyText = "";           //�����ı�����
        CoreProperties summary = null;  //�����ļ���Ϣ
        XSLFPowerPointExtractor docx = null;  //�ļ�����

        //�����ļ�
        try
        {
            docx = new XSLFPowerPointExtractor(new XSLFSlideShow(
                    file.getAbsolutePath()));
        }
        catch (Exception e)
        {
            throw new FileHandlerException(
                    "��ȡ�ļ����ִ���"
                    + file.getAbsolutePath(), e);
        }

        //��ȡ�ı�
        bodyText = docx.getText();

        //��ȡ�ļ���Ϣ
        summary = docx.getCoreProperties();

        //����Document����
        Document doc = new Document();
        //�����ļ����ֶ�
        doc.add(new Field("filename", file.getName(), Field.Store.YES,
                Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        //�����ļ������ֶ�
        doc.add(new Field("type", "ppt", Field.Store.NO,
                Field.Index.NOT_ANALYZED));
        //�����޸������ֶ�
        doc.add(new Field("date", Utility.getFormattedDate(
                summary.getModified()), Field.Store.YES,
                Field.Index.NO));
        //�����ļ�·��
        doc.add(new Field("path", file.getAbsolutePath(), Field.Store.YES,
                Field.Index.NOT_ANALYZED));
        //���ⲻΪ�գ����ӱ����ֶ�
        if (summary.getTitle() != null)
        {
            doc.add(new Field("title", summary.getTitle(), Field.Store.YES,
                    Field.Index.ANALYZED,
                    Field.TermVector.WITH_POSITIONS_OFFSETS));
        }
        //���߲�Ϊ�գ����������ֶ�
        if (summary.getCreator() != null)
        {
            doc.add(new Field("author", summary.getCreator(), Field.Store.YES,
                    Field.Index.NOT_ANALYZED,
                    Field.TermVector.WITH_POSITIONS_OFFSETS));
        }
        //���Ĳ�Ϊ�գ����������ֶ�
        if (!bodyText.isEmpty())
        {
            doc.add(new Field("contents", bodyText, Field.Store.YES,
                    Field.Index.ANALYZED,
                    Field.TermVector.WITH_POSITIONS_OFFSETS));
        }
        return doc;
    }
}