package org.ds.handlingtypes;

import java.io.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.ds.handlingtypes.filehandler.*;

/**
 * 处理纯文本文件
 */
public class PlainTextHandler implements FileHandler
{

    /**
     * 从纯文本文件获取一个Document类的实例
     * @param file 文件对象
     * @return 一个新的Document类的实例
     * @throws FileHandlerException DocumentHandler异常
     */
    @Override
    public Document getDocument(File file)
            throws FileHandlerException
    {
        String bodyText = Utility.readTextFile(file);
        Document doc = new Document();
        //添加文件名字段
        doc.add(new Field("filename", file.getName(), Field.Store.YES,
                Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        //添加文件类型字段
        doc.add(new Field("type", "txt", Field.Store.NO,
                Field.Index.NOT_ANALYZED));
        //添加文件修改日期字段
        doc.add(new Field("date", Utility.getLastModifiedDate(file),
                Field.Store.YES, Field.Index.NO));
        //添加文件路径
        doc.add(new Field("path", file.getAbsolutePath(), Field.Store.YES,
                Field.Index.NOT_ANALYZED));
        //正文不为空，添加正文字段
        if (bodyText != null)
        {
            doc.add(new Field("contents", bodyText, Field.Store.YES,
                    Field.Index.ANALYZED,
                    Field.TermVector.WITH_POSITIONS_OFFSETS));
        }
        return doc;
    }
}
