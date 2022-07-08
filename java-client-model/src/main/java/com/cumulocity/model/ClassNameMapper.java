package com.cumulocity.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.TypeMapper;
import org.svenson.tokenize.JSONTokenizer;

import com.cumulocity.model.util.ExtensibilityConverter;

public class ClassNameMapper implements TypeMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ClassNameMapper.class);

    @SuppressWarnings("rawtypes")
    @Override
    public Class getTypeHint(JSONTokenizer tokenizer, String parsePathInfo, Class typeHint) {
        int i = parsePathInfo.lastIndexOf(".");
        if (i >= 0) {
            String clazzPart = parsePathInfo.substring(i + 1);

            try {
                if (ExtensibilityConverter.isConvertable(clazzPart)) {
                    return ExtensibilityConverter.classFromExtensibilityString(clazzPart);
                }
            } catch (ClassNotFoundException e) {
                /* class could not be found
                   For properties, and in the weak-type case then this is entirely expected
                   but *could* be an issue if a strong-typed class was intended. Since
                   intention cannot be known, and to prevent log files filling up, 
                   logging is added at TRACE level */
                LOG.trace("Class {} not found using {}. Using typeHint {}", new Object[]
                        {clazzPart, Thread.currentThread().getContextClassLoader(), typeHint});
            }
        }
        return typeHint;
    }
}
